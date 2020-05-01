package br.com.codenation;

import br.com.codenation.desafio.annotation.Desafio;
import br.com.codenation.desafio.app.MeuTimeInterface;
import br.com.codenation.desafio.exceptions.CapitaoNaoInformadoException;
import br.com.codenation.desafio.exceptions.IdentificadorUtilizadoException;
import br.com.codenation.desafio.exceptions.JogadorNaoEncontradoException;
import br.com.codenation.desafio.exceptions.TimeNaoEncontradoException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DesafioMeuTimeApplication implements MeuTimeInterface {

	List<Time> times = new ArrayList<>();
	List<Jogador> jogadores = new ArrayList<>();

	@Desafio("incluirTime")
	public void incluirTime(Long id, String nome, LocalDate dataCriacao, String corUniformePrincipal, String corUniformeSecundario) {
		if (id != null) {
			if(buscarTime(id) != null) {
				throw new IdentificadorUtilizadoException("Id desse time já existe.");
			}
		}
		times.add(new Time(id, nome, dataCriacao, corUniformePrincipal, corUniformeSecundario));
	}

	private Time buscarTime(Long id) {
		for (Time time:times) {
			if(time.getId().equals(id)) {
				return time;
			}
		}
		return null;
	}

	@Desafio("incluirJogador")
	public void incluirJogador(Long id, Long idTime, String nome, LocalDate dataNascimento, Integer nivelHabilidade, BigDecimal salario) {
		if (id != null) {
			if (buscarJogador(id) != null) {
				throw new IdentificadorUtilizadoException("Id desse jogador já existe.");
			}
			if (idTime != null) {
				if (buscarTime(idTime) != null) {
					jogadores.add(new Jogador(id, idTime, nome, dataNascimento, nivelHabilidade, salario));
				} else {
					throw new TimeNaoEncontradoException("Time não encontrado.");
				}
			}
		}
	}

	private Jogador buscarJogador(Long id) {
		for (Jogador jogador:jogadores) {
			if(jogador.getId().equals(id)) {
				return jogador;
			}
		}
		return null;
	}

	@Desafio("definirCapitao")
	public void definirCapitao(Long idJogador) {
		Jogador jogador = buscarJogador(idJogador);
		if(jogador == null) {
			throw new JogadorNaoEncontradoException("Jogador não encontrado.");
		} else {
			Long idTime = jogador.getIdTime();
			Time time = buscarTime(idTime);
			if(time == null) {
				throw new TimeNaoEncontradoException("Time não encontrado.");
			} else {
				time.setIdCapitao(idJogador);
			}
		}
	}

	@Desafio("buscarCapitaoDoTime")
	public Long buscarCapitaoDoTime(Long idTime) {
		Time time = buscarTime(idTime);
		if(time == null) {
			throw new TimeNaoEncontradoException("Time não encontrado.");
		} else {
			if(time.getIdCapitao() == null) {
				throw new CapitaoNaoInformadoException("O time não tem capitão.");
			} else {
				return time.getIdCapitao();
			}
		}
	}

	@Desafio("buscarNomeJogador")
	public String buscarNomeJogador(Long idJogador) {
		Jogador jogador = buscarJogador(idJogador);
		if(jogador == null) {
			throw new JogadorNaoEncontradoException("Jogador não encontrado.");
		} else {
			return jogador.getNome();
		}
	}

	@Desafio("buscarNomeTime")
	public String buscarNomeTime(Long idTime) {
		Time time = buscarTime(idTime);
		if(time == null) {
			throw new TimeNaoEncontradoException("Time não encontrado.");
		} else {
			return time.getNome();
		}
	}

	@Desafio("buscarJogadoresDoTime")
	public List<Long> buscarJogadoresDoTime(Long idTime) {
		List<Long> listaIds= new ArrayList<>();
		for (Jogador jogador:jogadores) {
			if(jogador.getIdTime().equals(idTime)) {
				listaIds.add(jogador.getId());
			}
		}
		if(listaIds.isEmpty()) {
			throw new TimeNaoEncontradoException("Time não encontrado.");
		} else {
			return listaIds;
		}
	}

	@Desafio("buscarMelhorJogadorDoTime")
	public Long buscarMelhorJogadorDoTime(Long idTime) {
		Integer nivelHabilidade = null;
		Long id = null;
		Time time = buscarTime(idTime);
		if(time == null) {
			throw new TimeNaoEncontradoException("Time não encontrado.");
		}
		List<Jogador> jogadoresDoTime = jogadoresDoTime(idTime);
		for (Jogador jogador:jogadoresDoTime) {
			if(nivelHabilidade == null || jogador.getNivelHabilidade() > nivelHabilidade ) {
				nivelHabilidade = jogador.getNivelHabilidade();
				id = jogador.getId();
			}
		}
		return id;
	}

	@Desafio("buscarJogadorMaisVelho")
	public Long buscarJogadorMaisVelho(Long idTime) {
		LocalDate dataNascimento = LocalDate.now();
		Long id = null;
		Time time = buscarTime(idTime);
		if(time == null) {
			throw new TimeNaoEncontradoException("Time não encontrado.");
		}
		List<Jogador> jogadoresDoTime = jogadoresDoTime(idTime);
		for (Jogador jogador:jogadoresDoTime) {
			if(dataNascimento.isAfter(jogador.getDataNascimento())) {
				dataNascimento = jogador.getDataNascimento();
				id = jogador.getId();
			} else if(dataNascimento.isEqual(jogador.getDataNascimento())&&
					(id != null && jogador.getId() < id)) {
				dataNascimento = jogador.getDataNascimento();
				id = jogador.getId();
			}
		}
		return id;
	}

	@Desafio("buscarTimes")
	public List<Long> buscarTimes() {
		List<Long> listaTimesId = new ArrayList<>();
		times.sort(Comparator.comparing(Time::getId));
		for (Time time:times) {
			listaTimesId.add(time.getId());
		}
		return listaTimesId;
	}

	@Desafio("buscarJogadorMaiorSalario")
	public Long buscarJogadorMaiorSalario(Long idTime) {
		BigDecimal maiorSalario = new BigDecimal("0.00");
		Long id = null;
		Time time = buscarTime(idTime);
		if(time == null) {
			throw new TimeNaoEncontradoException("Time não encontrado.");
		}
		List<Jogador> jogadoresDoTime = jogadoresDoTime(idTime);
		for (Jogador jogador:jogadoresDoTime) {
			if(jogador.getSalario().compareTo(maiorSalario) > 0 ||
					(jogador.getSalario().compareTo(maiorSalario) == 0 &&
							(id == null || jogador.getId()<id))) {
				maiorSalario = jogador.getSalario();
				id = jogador.getId();
			}
		}
		return id;
	}

	private List<Jogador> jogadoresDoTime(Long idTime) {
		if(times == null || times.isEmpty()) {
			throw new TimeNaoEncontradoException("Time não encontrado.");
		} else {
			List<Jogador> jogadoresDoTime = new ArrayList<>();
			for (Jogador jogador:jogadores) {
				if(jogador.getIdTime().equals(idTime)) {
					jogadoresDoTime.add(jogador);
				}
			}
			return jogadoresDoTime;
		}

	}

	@Desafio("buscarSalarioDoJogador")
	public BigDecimal buscarSalarioDoJogador(Long idJogador) {
		Jogador jogador = buscarJogador(idJogador);
		if(jogador == null) {
			throw new JogadorNaoEncontradoException("Jogador não encontrado.");
		} else {
			return jogador.getSalario();
		}
	}

	@Desafio("buscarTopJogadores")
	public List<Long> buscarTopJogadores(Integer top) {
		List<Long> topList = new ArrayList<>();
		Collections.sort(jogadores);
		for(int i=0; i<top; i++){
			topList.add(jogadores.get(i).getId());
		}
		return topList;
	}

	@Desafio("buscarCorCamisaTimeDeFora")
	public String buscarCorCamisaTimeDeFora(Long timeDaCasa, Long timeDeFora) {
		Time timeCasa = buscarTime(timeDaCasa);
		Time timeFora = buscarTime(timeDeFora);
		if(timeCasa != null && timeFora != null) {
			if(timeCasa.getCorUniformePrincipal().equals(timeFora.getCorUniformePrincipal())) {
				return timeFora.getCorUniformeSecundario();
			} else {
				return timeFora.getCorUniformePrincipal();
			}
		} else {
			throw new TimeNaoEncontradoException("Time não encontrado.");
		}
	}

}