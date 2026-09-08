package sistemaNotas;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.math.BigDecimal;

import java.sql.SQLException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class TelaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    // DAOs
    private final AlunoDAO alunoDAO =
            new AlunoDAO();

    private final DisciplinaDAO disciplinaDAO =
            new DisciplinaDAO();

    private final TipoProvaDAO tipoProvaDAO =
            new TipoProvaDAO();

    private final NotaDAO notaDAO =
            new NotaDAO();

    // Cadastro de aluno
    private JTextField txtRa;
    private JTextField txtNome;
    private JTextField txtDataNascimento;
    private JTextField txtRg;

    // Lançamento
    private JComboBox<Aluno> cbAlunoNota;
    private JComboBox<Disciplina> cbDisciplinaNota;
    private JComboBox<TipoProva> cbTipoNota;
    private JTextField txtNota;

    // Consulta
    private JComboBox<Aluno> cbAlunoConsulta;
    private JComboBox<Disciplina> cbDisciplinaConsulta;
    private JComboBox<TipoProva> cbTipoConsulta;

    // Boletim
    private JComboBox<Aluno> cbAlunoBoletim;

    public TelaPrincipal() {

        setTitle(
                "Sistema de Gerenciamento de Notas");

        setSize(750, 520);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        criarInterface();

        carregarDadosIniciais();
    }

    private void criarInterface() {

        JTabbedPane abas =
                new JTabbedPane();

        abas.addTab(
                "Cadastrar Aluno",
                criarPainelCadastro());

        abas.addTab(
                "Lançar Nota",
                criarPainelNota());

        abas.addTab(
                "Consultar Nota",
                criarPainelConsulta());

        abas.addTab(
                "Boletim",
                criarPainelBoletim());

        add(abas, BorderLayout.CENTER);
    }

    // ==================================================
    // CADASTRO DE ALUNOS
    // ==================================================

    private JPanel criarPainelCadastro() {

        JPanel painel =
                criarPainelFormulario();

        txtRa = new JTextField(20);
        txtNome = new JTextField(20);
        txtDataNascimento =
                new JTextField(20);
        txtRg = new JTextField(20);

        adicionarCampo(
                painel,
                "RA:",
                txtRa,
                0);

        adicionarCampo(
                painel,
                "Nome:",
                txtNome,
                1);

        adicionarCampo(
                painel,
                "Data de nascimento (dd/MM/yyyy):",
                txtDataNascimento,
                2);

        adicionarCampo(
                painel,
                "RG:",
                txtRg,
                3);

        JButton btnCadastrar =
                new JButton("Cadastrar Aluno");

        GridBagConstraints gbc =
                criarRestricoes();

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor =
                GridBagConstraints.CENTER;

        painel.add(btnCadastrar, gbc);

        btnCadastrar.addActionListener(
                e -> cadastrarAluno());

        return painel;
    }

    private void cadastrarAluno() {

        String ra =
                txtRa.getText().trim();

        String nome =
                txtNome.getText().trim();

        String dataTexto =
                txtDataNascimento
                    .getText()
                    .trim();

        String rg =
                txtRg.getText().trim();

        /*
         * Primeira validação:
         * campos obrigatórios.
         */
        if (ra.isEmpty()
                || nome.isEmpty()
                || dataTexto.isEmpty()
                || rg.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Preencha todos os campos.",
                    "Campos obrigatórios",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        /*
         * O banco define RA como VARCHAR(8).
         * Por isso exigimos oito caracteres.
         */
        if (ra.length() != 8) {

            JOptionPane.showMessageDialog(
                    this,
                    "O RA deve possuir exatamente "
                    + "8 caracteres.",
                    "RA inválido",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        LocalDate dataNascimento;

        try {

            DateTimeFormatter formato =
                    DateTimeFormatter
                    .ofPattern("dd/MM/uuuu")
                    .withResolverStyle(
                            ResolverStyle.STRICT);

            dataNascimento =
                    LocalDate.parse(
                            dataTexto,
                            formato);

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Data inválida.\n"
                    + "Utilize o formato "
                    + "dd/MM/yyyy.",
                    "Data inválida",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        try {

            /*
             * Verifica se o RA já existe.
             */
            if (alunoDAO.existeRa(ra)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Já existe um aluno "
                        + "cadastrado com esse RA.",
                        "RA duplicado",
                        JOptionPane.WARNING_MESSAGE);

                return;
            }

            /*
             * Verifica se o RG já existe.
             */
            if (alunoDAO.existeRg(rg)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Já existe um aluno "
                        + "cadastrado com esse RG.",
                        "RG duplicado",
                        JOptionPane.WARNING_MESSAGE);

                return;
            }

            Aluno aluno =
                    new Aluno(
                        ra,
                        nome,
                        dataNascimento,
                        rg);

            alunoDAO.inserir(aluno);

            JOptionPane.showMessageDialog(
                    this,
                    "Aluno cadastrado "
                    + "com sucesso.");

            limparCadastro();

            /*
             * Atualiza automaticamente
             * todos os JComboBox que
             * possuem alunos.
             */
            recarregarAlunos();

        } catch (SQLException e) {

            mostrarErroBanco(e);
        }
    }

    private void limparCadastro() {

        txtRa.setText("");
        txtNome.setText("");
        txtDataNascimento.setText("");
        txtRg.setText("");

        txtRa.requestFocus();
    }

    // ==================================================
    // LANÇAMENTO DE NOTAS
    // ==================================================

    private JPanel criarPainelNota() {

        JPanel painel =
                criarPainelFormulario();

        cbAlunoNota =
                new JComboBox<>();

        cbDisciplinaNota =
                new JComboBox<>();

        cbTipoNota =
                new JComboBox<>();

        txtNota =
                new JTextField(20);

        adicionarCampo(
                painel,
                "Aluno:",
                cbAlunoNota,
                0);

        adicionarCampo(
                painel,
                "Disciplina:",
                cbDisciplinaNota,
                1);

        adicionarCampo(
                painel,
                "Tipo de prova:",
                cbTipoNota,
                2);

        adicionarCampo(
                painel,
                "Nota:",
                txtNota,
                3);

        JButton btnLancar =
                new JButton("Lançar Nota");

        GridBagConstraints gbc =
                criarRestricoes();

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor =
                GridBagConstraints.CENTER;

        painel.add(btnLancar, gbc);

        btnLancar.addActionListener(
                e -> lancarNota());

        return painel;
    }

    private void lancarNota() {

        Aluno aluno =
                (Aluno)
                cbAlunoNota.getSelectedItem();

        Disciplina disciplina =
                (Disciplina)
                cbDisciplinaNota
                .getSelectedItem();

        TipoProva tipo =
                (TipoProva)
                cbTipoNota.getSelectedItem();

        String notaTexto =
                txtNota.getText()
                       .trim();

        if (aluno == null
                || disciplina == null
                || tipo == null
                || notaTexto.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Preencha todas as "
                    + "informações da nota.",
                    "Campos obrigatórios",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        BigDecimal nota;

        try {

            /*
             * Permite que o usuário
             * digite 8,5 ou 8.5.
             */
            notaTexto =
                    notaTexto.replace(
                            ",", ".");

            nota =
                    new BigDecimal(notaTexto);

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe uma nota válida.",
                    "Nota inválida",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        /*
         * Evita mais de duas casas decimais.
         */
        if (nota.scale() > 2) {

            JOptionPane.showMessageDialog(
                    this,
                    "A nota pode possuir "
                    + "no máximo duas "
                    + "casas decimais.",
                    "Nota inválida",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        /*
         * Verifica intervalo 0 a 10.
         */
        if (nota.compareTo(
                BigDecimal.ZERO) < 0
                ||
            nota.compareTo(
                BigDecimal.TEN) > 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "A nota deve estar "
                    + "entre 0,00 e 10,00.",
                    "Nota fora do intervalo",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        try {

            /*
             * Evita combinação duplicada:
             *
             * aluno + disciplina + prova
             */
            boolean existe =
                    notaDAO.existeNota(

                        aluno.getRa(),

                        disciplina
                            .getIdDisciplina(),

                        tipo.getIdTipoProva()
                    );

            if (existe) {

                JOptionPane.showMessageDialog(
                        this,
                        "Já existe uma nota "
                        + "cadastrada para:\n\n"
                        + "Aluno: "
                        + aluno.getNome()
                        + "\nDisciplina: "
                        + disciplina
                            .getNomeDisciplina()
                        + "\nProva: "
                        + tipo.getNomeProva(),
                        "Nota duplicada",
                        JOptionPane.WARNING_MESSAGE);

                return;
            }

            notaDAO.inserir(

                    aluno.getRa(),

                    disciplina
                        .getIdDisciplina(),

                    tipo.getIdTipoProva(),

                    nota
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Nota cadastrada "
                    + "com sucesso.");

            txtNota.setText("");

        } catch (SQLException e) {

            mostrarErroBanco(e);
        }
    }

    // ==================================================
    // CONSULTA INDIVIDUAL
    // ==================================================

    private JPanel criarPainelConsulta() {

        JPanel painel =
                criarPainelFormulario();

        cbAlunoConsulta =
                new JComboBox<>();

        cbDisciplinaConsulta =
                new JComboBox<>();

        cbTipoConsulta =
                new JComboBox<>();

        adicionarCampo(
                painel,
                "Aluno:",
                cbAlunoConsulta,
                0);

        adicionarCampo(
                painel,
                "Disciplina:",
                cbDisciplinaConsulta,
                1);

        adicionarCampo(
                painel,
                "Tipo de prova:",
                cbTipoConsulta,
                2);

        JButton btnConsultar =
                new JButton("Consultar Nota");

        GridBagConstraints gbc =
                criarRestricoes();

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor =
                GridBagConstraints.CENTER;

        painel.add(btnConsultar, gbc);

        btnConsultar.addActionListener(
                e -> consultarNota());

        return painel;
    }

    private void consultarNota() {

        Aluno aluno =
                (Aluno)
                cbAlunoConsulta
                .getSelectedItem();

        Disciplina disciplina =
                (Disciplina)
                cbDisciplinaConsulta
                .getSelectedItem();

        TipoProva tipo =
                (TipoProva)
                cbTipoConsulta
                .getSelectedItem();

        if (aluno == null
                || disciplina == null
                || tipo == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione aluno, "
                    + "disciplina e prova.",
                    "Consulta",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        try {

            ResultadoNota resultado =
                    notaDAO.consultar(

                        aluno.getRa(),

                        disciplina
                            .getIdDisciplina(),

                        tipo.getIdTipoProva()
                    );

            if (resultado == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Não existe nota "
                        + "cadastrada para a "
                        + "combinação selecionada.",
                        "Consulta sem resultado",
                        JOptionPane.INFORMATION_MESSAGE);

                return;
            }

            DialogResultadoNota dialog =
                    new DialogResultadoNota(
                            this,
                            resultado);

            dialog.setVisible(true);

        } catch (SQLException e) {

            mostrarErroBanco(e);
        }
    }

    // ==================================================
    // BOLETIM
    // ==================================================

    private JPanel criarPainelBoletim() {

        JPanel painel =
                criarPainelFormulario();

        cbAlunoBoletim =
                new JComboBox<>();

        adicionarCampo(
                painel,
                "Aluno:",
                cbAlunoBoletim,
                0);

        JButton btnBoletim =
                new JButton("Visualizar Boletim");

        GridBagConstraints gbc =
                criarRestricoes();

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor =
                GridBagConstraints.CENTER;

        painel.add(btnBoletim, gbc);

        btnBoletim.addActionListener(
                e -> abrirBoletim());

        return painel;
    }

    private void abrirBoletim() {

        Aluno aluno =
                (Aluno)
                cbAlunoBoletim
                .getSelectedItem();

        if (aluno == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um aluno.",
                    "Boletim",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        try {

            List<ResultadoNota> notas =
                    notaDAO.listarBoletim(
                            aluno.getRa());

            if (notas.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "O aluno selecionado "
                        + "ainda não possui notas.",
                        "Boletim",
                        JOptionPane.INFORMATION_MESSAGE);

                return;
            }

            DialogBoletim dialog =
                    new DialogBoletim(
                            this,
                            aluno,
                            notas);

            dialog.setVisible(true);

        } catch (SQLException e) {

            mostrarErroBanco(e);
        }
    }

    // ==================================================
    // CARREGAMENTO DOS COMBOBOX
    // ==================================================

    private void carregarDadosIniciais() {

        try {

            recarregarAlunos();

            carregarDisciplinas();

            carregarTiposProva();

        } catch (SQLException e) {

            mostrarErroBanco(e);
        }
    }

    private void recarregarAlunos()
            throws SQLException {

        List<Aluno> alunos =
                alunoDAO.listar();

        cbAlunoNota.removeAllItems();
        cbAlunoConsulta.removeAllItems();
        cbAlunoBoletim.removeAllItems();

        for (Aluno aluno : alunos) {

            cbAlunoNota.addItem(aluno);
            cbAlunoConsulta.addItem(aluno);
            cbAlunoBoletim.addItem(aluno);
        }
    }

    private void carregarDisciplinas()
            throws SQLException {

        List<Disciplina> disciplinas =
                disciplinaDAO.listar();

        cbDisciplinaNota.removeAllItems();
        cbDisciplinaConsulta.removeAllItems();

        for (Disciplina disciplina :
                disciplinas) {

            cbDisciplinaNota
                    .addItem(disciplina);

            cbDisciplinaConsulta
                    .addItem(disciplina);
        }
    }

    private void carregarTiposProva()
            throws SQLException {

        List<TipoProva> tipos =
                tipoProvaDAO.listar();

        cbTipoNota.removeAllItems();
        cbTipoConsulta.removeAllItems();

        for (TipoProva tipo : tipos) {

            cbTipoNota.addItem(tipo);
            cbTipoConsulta.addItem(tipo);
        }
    }

    // ==================================================
    // MÉTODOS AUXILIARES DA INTERFACE
    // ==================================================

    private JPanel criarPainelFormulario() {

        JPanel painel =
                new JPanel(
                        new GridBagLayout());

        painel.setBorder(
                BorderFactory
                .createEmptyBorder(
                        30, 40, 30, 40));

        return painel;
    }

    private GridBagConstraints criarRestricoes() {

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        8, 8, 8, 8);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        return gbc;
    }

    private void adicionarCampo(
            JPanel painel,
            String texto,
            java.awt.Component componente,
            int linha) {

        GridBagConstraints gbc =
                criarRestricoes();

        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.weightx = 0;

        painel.add(
                new JLabel(texto),
                gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;

        painel.add(
                componente,
                gbc);
    }

    // ==================================================
    // TRATAMENTO DE ERROS DE BANCO
    // ==================================================

    private void mostrarErroBanco(SQLException e) {

        JOptionPane.showMessageDialog(
                this,
                "Não foi possível concluir a operação no banco de dados."
                + "\n\nDetalhes técnicos:"
                + "\n" + e.getMessage(),
                "Erro no banco de dados",
                JOptionPane.ERROR_MESSAGE
        );

        e.printStackTrace();
    }
}