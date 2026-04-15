package com.starterkit.springboot.todo;

import java.util.List;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.validation.BindingResult;


@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoRepository repo;

    public TodoController(TodoRepository repo) {
        this.repo = repo;
    }



@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public Todo create(@RequestBody @Valid TodoRequest req, BindingResult br) {

    // Isto garante que entras no método e consegues ver o erro no debugger
    if (br.hasErrors()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, br.getAllErrors().toString());
    }

    Todo t = new Todo();
    t.setTitle(req.getTitle());
    t.setDescription(req.getDescription());

    if (req.getDone() != null) {
        t.setDone(req.getDone());
    }

    return repo.save(t);
}


    @GetMapping
    public List<Todo> list() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Todo get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo não encontrado"));
    }

    @PutMapping("/{id}")
    public Todo update(@PathVariable Long id, @Valid @RequestBody TodoRequest req) {
        Todo t = repo.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo não encontrado"));

        t.setTitle(req.getTitle());
        t.setDescription(req.getDescription());
        if (req.getDone() != null) {
            t.setDone(req.getDone());
        }
        return repo.save(t);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo não encontrado");
        }
        repo.deleteById(id);
    }
}
