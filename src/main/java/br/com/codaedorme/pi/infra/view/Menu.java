package br.com.codaedorme.pi.infra.view;

import br.com.codaedorme.pi.domain.produto.ProdutoMenu;
import br.com.codaedorme.pi.domain.usuario.Session;
import br.com.codaedorme.pi.domain.usuario.Usuario;
import br.com.codaedorme.pi.domain.usuario.UsuarioMenu;
import br.com.codaedorme.pi.domain.usuario.UsuarioService;
import br.com.codaedorme.pi.domain.usuario.enums.Grupo;
import br.com.codaedorme.pi.domain.usuario.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Scanner;

@Component
public class Menu {
    private static final Scanner SCANNER = new Scanner(System.in);

    @Autowired
    private Session session;

    @Autowired
    private UsuarioService service;

    @Autowired
    private UsuarioMenu usuarioMenu;

    @Autowired
    private ProdutoMenu produtoMenu;

    public void menu() {
        boolean rodando = true;
        int escolha;
        String menu = "1 - Lista Produtos\n2 - Listar Usuários\n3 - Sair";

        while (rodando) {
            System.out.println("DADOS DA SESSÃO: ");
            System.out.println("Usuário: " + session.getUsuario().getNome()+
                    " | E-mail: " + session.getUsuario().getEmail() +
                    " | Grupo: " + session.getUsuario().getGrupo());

            System.out.println(menu);
            escolha = SCANNER.nextInt();
            SCANNER.nextLine();

            switch (escolha) {
                case 1:
                    produtoMenu.listarProdutos();
                    break;
                case 2:
                    usuarioMenu.listarUsuarios();
                    break;
                case 3:
                    System.out.println("------ Finalizando sessão! Até mais! ------");
                    session.logout();
                    rodando = false;
                    break;
                default:
                    System.out.println("Essa opcao nao existe");
                    break;
            }
        }
    }

    public void inicio(){
        inicializarUsuarioAdministrador();

        boolean rodando = true;
        int escolha;
        String menu = "1 - Login\n2 - Desligar";

        while (rodando) {
            System.out.println(menu);
            escolha = SCANNER.nextInt();
            SCANNER.nextLine();

            switch (escolha) {
                case 1:
                    login();
                    break;
                case 2:
                    System.out.println("------ Tchau até mais ------");
                    rodando = false;
                    break;
                default:
                    System.out.println("Essa opcao nao existe");
                    break;
            }
        }
    }

    public void login() {
        System.out.println("------ LOGIN ------");

        System.out.println("Digite seu email:");
        String email = SCANNER.nextLine();

        System.out.println("Digite sua senha:");
        String senha = SCANNER.nextLine();

        Optional<Usuario> usuario = service.login(email, senha);

        if (usuario.isPresent() && usuario.get().getStatus() == Status.ATIVO) {
            System.out.println("\nLogin bem-sucedido! Bem vindo " + usuario.get().getNome());
            session.setUsuario(usuario.get());
            usuarioMenu.setSession(session);
            produtoMenu.setSession(session);
            menu();
            return;
        }

        System.out.println("\nEmail ou senha incorretos, ou usuario inativo1. Tente novamente.");
        login();
    }

    private void inicializarUsuarioAdministrador(){
        if (service.findAll().length == 0) {
            Grupo grupo = Grupo.valueOf("ADMINISTRADOR");
            Usuario usuarioAdm = new Usuario();
            usuarioAdm.setEmail("admin@admin");
            usuarioAdm.setNome("Administrador");
            usuarioAdm.setCpf("11111111111");
            usuarioAdm.setGrupo(grupo);
            usuarioAdm.setSenha("admin123");
            usuarioAdm.setStatus(Status.ATIVO);

            service.save(usuarioAdm, "admin123");
        }
    }

}
