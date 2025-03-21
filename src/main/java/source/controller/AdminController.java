package source.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import source.model.Cliente;
import source.repository.ClienteRepository;

import java.util.List;

//CRUD do projeto

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping({"", "/"})
    public String adminPage(Model model) {
        List<Cliente> usuarios = clienteRepository.findAll();
        model.addAttribute("usuarios", usuarios);
        return "admin";
    }

    // Editar usuário
    @GetMapping("/editar-usuario/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Cliente usuario = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        model.addAttribute("usuario", usuario);
        return "editar-usuario";
    }

    // Atualizar usuário
    @PostMapping("/atualizar-usuario/{id}")
    public String atualizarUsuario(@PathVariable Long id, @ModelAttribute Cliente usuario) {
        Cliente usuarioExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuarioExistente.setNome(usuario.getNome());
        usuarioExistente.setCpf(usuario.getCpf());
        usuarioExistente.setUsername(usuario.getUsername());
        clienteRepository.save(usuarioExistente);
        return "redirect:/admin/";
    }


    // Página de confirmação de deleção de usuário
    @GetMapping("/deletar-usuario/{id}")
    public String deletarUsuarioPage(@PathVariable Long id, Model model) {
        Cliente usuario = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        model.addAttribute("usuario", usuario); // Adiciona o usuário ao modelo
        return "deletar-usuario"; // Retorna a página deletar-usuario.html
    }

    // Deletar usuário
    @PostMapping("/deletar-usuario/{id}")
    public String deletarUsuario(@PathVariable Long id) {
        clienteRepository.deleteById(id); // Deleta o usuário pelo ID
        return "redirect:/admin/"; // Redireciona de volta para a página de administração
    }
}