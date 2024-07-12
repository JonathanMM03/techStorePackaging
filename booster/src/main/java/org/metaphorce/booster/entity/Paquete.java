package org.metaphorce.booster.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Paquete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "idRemitente", nullable = false)
    @JsonBackReference
    private Usuario remitente;
    private char status;
    @Column(updatable = false)
    private String fechaEnvio;
    @NotBlank
    private String origen;
    @NotBlank
    private String destino;
    private double costo;
    private String fechaEstimada;
    private String tipo;
    @NotBlank
    private String contenido;
}
