package org.metaphorce.booster.controller;

import org.metaphorce.booster.entity.Paquete;
import org.metaphorce.booster.entity.Usuario;
import org.metaphorce.booster.repository.PaqueteRepository;
import org.metaphorce.booster.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/paquete")
@Validated
public class PaqueteController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PaqueteRepository paqueteRepository;

    @PostMapping("/send")
    public Usuario registerPackage(@PathVariable Long id, @RequestBody Paquete paquete){
        Optional<Usuario> usuario = usuarioRepository.findById(paquete.getRemitente().getId());
        if (usuario.isEmpty()){
            return null;
        }
        List<Paquete> paqueteList = usuario.get().getPaquetes();
        paqueteList.add(paquete);

        return usuarioRepository.save(usuario.get());
    }

    @PostMapping("/sendMany")
    public List<Usuario> registerPackage(@RequestBody List<Paquete> paquetes){
        List<Usuario> usuarios = new ArrayList<>();
        for(Paquete paquete1: paquetes){
            Optional<Usuario> usuario = usuarioRepository.findById(paquete1.getRemitente().getId());
            if (usuario.isPresent()){
                usuario.get().getPaquetes().add(paquete1);
                usuarios.add(usuarioRepository.save(usuario.get()));
            }
        }
        return usuarios;
    }

    @GetMapping("/listAll")
    public List<Paquete> listPackages(){
        return paqueteRepository.findAll();
    }

    @GetMapping("/listByStatus")
    public List<Paquete> listPackagesByEstatus(@RequestParam char estatus){
        return paqueteRepository.findByStatusOrderByFechaEnvioAsc(estatus);
    }

    @PostMapping("/attend")
    public ResponseEntity<String> attendPackages() {
        List<Paquete> pendingPackages = paqueteRepository.findByStatusOrderByFechaEnvioAsc('P');

        if (pendingPackages.isEmpty()) {
            return ResponseEntity.ok("No pending packages to attend.");
        }

        int numberOfThreads = Math.min(3, pendingPackages.size());
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        for (Paquete paquete : pendingPackages) {
            executorService.execute(() -> {
                attendPackage(paquete);
            });
        }

        executorService.shutdown();
        return ResponseEntity.ok("Pending packages are being attended.");
    }

    private void attendPackage(Paquete paquete) {
        // Simulate attending the package, e.g., updating its status
        paquete.setStatus('E'); // Assuming 'A' means attended
        paqueteRepository.save(paquete);
    }
}
