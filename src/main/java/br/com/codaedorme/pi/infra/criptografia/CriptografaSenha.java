package br.com.codaedorme.pi.infra.criptografia;

import org.springframework.stereotype.Service;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Service
public class CriptografaSenha {
    public String criptografar(String senha) {
        return BCrypt.withDefaults().hashToString(12, senha.toCharArray());
    }
}
