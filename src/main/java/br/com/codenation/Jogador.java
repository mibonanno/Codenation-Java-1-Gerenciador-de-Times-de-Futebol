package br.com.codenation;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Jogador implements Comparable<Jogador>{
    private Long id;
    private Long idTime;
    private String nome;
    private LocalDate dataNascimento;
    private Integer nivelHabilidade;
    private BigDecimal salario;

    public Jogador(Long id, Long idTime, String nome, LocalDate dataNascimento, Integer nivelHabilidade, BigDecimal salario) {
        this.id = id;
        this.idTime = idTime;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        nivelHabilidade = nivelHabilidade > 100 ? 100 : nivelHabilidade;
        nivelHabilidade = nivelHabilidade < 0 ? 0 : nivelHabilidade;
        this.nivelHabilidade = nivelHabilidade;
        this.salario = salario;
    }

    public Long getId() {
        return id;
    }

    public Long getIdTime() {
        return idTime;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public Integer getNivelHabilidade() {
        return nivelHabilidade;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    @Override
    public int compareTo(Jogador outroJogador) {
        int resultado = outroJogador.getNivelHabilidade().compareTo(this.nivelHabilidade);
        if(resultado != 0) {
            return resultado;
        } else {
            return this.id.compareTo(outroJogador.getId());
        }
    }
}

