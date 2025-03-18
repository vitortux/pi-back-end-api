package br.com.codaedorme.pi.infra.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.codaedorme.pi.domain.usuario.Usuario;
import br.com.codaedorme.pi.domain.usuario.UsuarioRepository;

@Service
public class ValidaSenha {

    @Autowired
    private UsuarioRepository repository;

    public boolean validaSenhas(String senha1, String senha2) {
        return (senha1.equals(senha2));
    }

    public boolean validaHash(String senhaRecebida, Long usuarioId) {
        Usuario usuario = repository.findById(usuarioId).orElse(null);

        if (usuario == null) {
            return false;
        }

        String senhaCriptografada = usuario.getSenha();

        return BCrypt.verifyer().verify(senhaRecebida.toCharArray(), senhaCriptografada.getBytes()).verified;
    }
}
