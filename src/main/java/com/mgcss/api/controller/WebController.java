package com.mgcss.api.controller;

import com.mgcss.domain.Cliente;
import com.mgcss.domain.EstadoSolicitud;
import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.domain.TipoCliente;
import com.mgcss.api.dto.ClienteDTO;
import com.mgcss.service.SolicitudService;
import com.mgcss.service.TecnicoService;
import com.mgcss.service.ClienteService;
import com.mgcss.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {

    private final SolicitudService solicitudService;
    private final TecnicoService tecnicoService;
    private final ClienteService clienteService;
    private final DashboardService dashboardService;

    public WebController(SolicitudService solicitudService, TecnicoService tecnicoService, ClienteService clienteService, DashboardService dashboardService) {
        this.solicitudService = solicitudService;
        this.tecnicoService = tecnicoService;
        this.clienteService = clienteService;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/solicitudes";
    }

    @GetMapping("/solicitudes")
    public String solicitudes(Model model) {
        model.addAttribute("solicitudes", solicitudService.listarSolicitudes());
        model.addAttribute("tecnicos", tecnicoService.listarTecnicos());
        model.addAttribute("clientes", clienteService.listarClientes());
        return "solicitudes";
    }

    @PostMapping("/solicitudes/crear")
    public String crearSolicitud(@RequestParam("descripcion") String descripcion,
                                 @RequestParam("clienteId") Long clienteId,
                                 @RequestParam(value = "tecnicoId", required = false) Long tecnicoId,
                                 RedirectAttributes redirectAttributes) {
        try {
            Solicitud solicitud = new Solicitud();
            solicitud.setDescripcion(descripcion);
            
            ClienteDTO cliDto = clienteService.consultarCliente(clienteId).orElseThrow(() -> new IllegalArgumentException("Cliente no existe"));
            Cliente cli = new Cliente();
            cli.setId(cliDto.getId());
            cli.setNombre(cliDto.getNombre());
            cli.setEmail(cliDto.getEmail());
            cli.setTipoCliente(cliDto.getTipoCliente());
            solicitud.setCliente(cli);
            
            if (tecnicoId != null) {
                Tecnico tec = tecnicoService.consultarTecnico(tecnicoId);
                solicitud.setTecnico(tec);
                solicitud.setEstado(EstadoSolicitud.EN_PROCESO);
            }
            
            solicitudService.crearSolicitud(solicitud);
            redirectAttributes.addFlashAttribute("exito", "Solicitud creada con éxito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear la solicitud: " + e.getMessage());
        }
        return "redirect:/solicitudes";
    }

    @PostMapping("/solicitudes/{id}/procesar")
    public String procesarSolicitud(@PathVariable Long id, @RequestParam("tecnicoId") Long tecnicoId, RedirectAttributes redirectAttributes) {
        try {
            Tecnico tec = tecnicoService.consultarTecnico(tecnicoId);
            solicitudService.asignarTecnico(id, tec);
            redirectAttributes.addFlashAttribute("exito", "Solicitud procesada y asignada al técnico.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar: " + e.getMessage());
        }
        return "redirect:/solicitudes";
    }

    @PostMapping("/solicitudes/{id}/cerrar")
    public String cerrarSolicitud(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Solicitud sol = solicitudService.consultarSolicitud(id);
            if(sol.cerrarSolicitud()) {
                solicitudService.cambiarEstado(sol, EstadoSolicitud.CERRADA);
                redirectAttributes.addFlashAttribute("exito", "Solicitud cerrada.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Transición de estado no permitida.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/solicitudes";
    }

    @PostMapping("/solicitudes/{id}/reabrir")
    public String reabrirSolicitud(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            solicitudService.reabrirSolicitud(id);
            redirectAttributes.addFlashAttribute("exito", "Solicitud reabierta.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/solicitudes";
    }

    @GetMapping("/tecnicos")
    public String tecnicos(Model model) {
        model.addAttribute("tecnicos", tecnicoService.listarTecnicos());
        return "tecnicos";
    }

    @PostMapping("/tecnicos/crear")
    public String crearTecnico(@RequestParam("nombre") String nombre, @RequestParam("edad") int edad, RedirectAttributes redirectAttributes) {
        try {
            Tecnico tecnico = new Tecnico();
            tecnico.setNombre(nombre);
            tecnico.setEdad(edad);
            tecnicoService.crearTecnico(tecnico);
            redirectAttributes.addFlashAttribute("exito", "Técnico registrado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/tecnicos";
    }

    @PostMapping("/tecnicos/{id}/toggle-estado")
    public String toggleEstadoTecnico(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Tecnico tecnico = tecnicoService.consultarTecnico(id);
            tecnicoService.cambiarEstado(tecnico, !tecnico.isTecnicoActivo());
            redirectAttributes.addFlashAttribute("exito", "Estado del técnico cambiado");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/tecnicos";
    }

    @GetMapping("/clientes")
    public String clientes(Model model) {
        model.addAttribute("clientes", clienteService.listarClientes());
        return "clientes";
    }

    @PostMapping("/clientes/crear")
    public String crearCliente(@RequestParam("nombre") String nombre, @RequestParam("email") String email, @RequestParam("tipoCliente") TipoCliente tipoCliente, RedirectAttributes redirectAttributes) {
        try {
            ClienteDTO dto = new ClienteDTO();
            dto.setNombre(nombre);
            dto.setEmail(email);
            dto.setTipoCliente(tipoCliente);
            clienteService.crearCliente(dto);
            redirectAttributes.addFlashAttribute("exito", "Cliente registrado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/clientes";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("metricas", dashboardService.obtenerMetricas());
        return "dashboard";
    }
}
