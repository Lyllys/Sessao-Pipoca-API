package org.serratec.resources;

import java.util.List;
import java.util.Optional;

import org.serratec.dtos.FilmeCadastroDTO;
import org.serratec.exceptions.FilmeException;
import org.serratec.models.Filme;
import org.serratec.repositories.FilmeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilmeResource {
	
	@Autowired
	FilmeRepository repository;
	
	@GetMapping("/filmes")
	public ResponseEntity<?> getFilmes (){
		List<Filme> filmes = repository.findAll();
		return new ResponseEntity<>(filmes , HttpStatus.OK);
	}
	
	@GetMapping("/filmes/{id}")
	public ResponseEntity<?> getFilmesPorId (@PathVariable Long id){
		Optional<Filme> opcional = repository.findById(id);
		
		if(opcional.isEmpty())
			return new ResponseEntity<> ("Filme não encontrado" , HttpStatus.NOT_FOUND);
		
		Filme existente = opcional.get();
		
		return new ResponseEntity<> (existente, HttpStatus.OK);
	}
	
	
	@PostMapping("/filmes")
	public ResponseEntity<?> postFilmes(@Validated @RequestBody FilmeCadastroDTO filmeDto){
		Filme filme = filmeDto.toFilme();
		
		try {
			repository.save(filme);
			return new ResponseEntity<> ("Filme cadastrado com sucesso!" , HttpStatus.OK);
			
		}catch(DataIntegrityViolationException e){
			if(repository.existsByNome(filme.getNome()))
				return new ResponseEntity<> ("Já existe um filme cadastrado com esse nome", HttpStatus.BAD_REQUEST);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PutMapping("filmes/{id}")
	public ResponseEntity<?> putFilmes(@PathVariable Long id , @RequestBody Filme novo) throws FilmeException{
		try {
			Filme filme = repository.findById(id).orElseThrow(() -> new FilmeException ("Filme não encontrado"));
			
			if(novo.getNome() !=null && !novo.getNome().isBlank())
				filme.setNome(novo.getNome());
			
			if(novo.getClassificacao() !=null && !novo.getClassificacao().isBlank())
				filme.setClassificacao(novo.getClassificacao());
			
			if(novo.getComentario() !=null && !novo.getComentario().isBlank())
				filme.setComentario(novo.getComentario());
			
			repository.save(filme);
			
			return new ResponseEntity<> ("Atualizações realizadas com sucesso" , HttpStatus.OK);
		}catch(DataIntegrityViolationException e) {
			return new ResponseEntity<> ("Erro ao atualizar" , HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@DeleteMapping("/filmes/{id}")
	public ResponseEntity<?> deleteFilmes(@PathVariable Long id){
		Optional<Filme> opcional = repository.findById(id);
		
		if(opcional.isEmpty())
			return new ResponseEntity<>("Filme não encontrado" , HttpStatus.NOT_FOUND);
		
		Filme existente = opcional.get();
		repository.delete(existente);
		
		return new ResponseEntity<>("Filme deletado com sucesso", HttpStatus.OK);
	}
	

}
