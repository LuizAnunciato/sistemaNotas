package sistemaNotas;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {

        /*
         * SwingUtilities.invokeLater garante
         * que a interface seja criada na
         * Event Dispatch Thread do Swing.
         */
        SwingUtilities.invokeLater(() -> {

            try {

                /*
                 * Utiliza a aparência padrão
                 * do sistema operacional.
                 */
                UIManager.setLookAndFeel(
                    UIManager
                    .getSystemLookAndFeelClassName()
                );

            } catch (Exception e) {

                e.printStackTrace();
            }

            TelaPrincipal tela =
                    new TelaPrincipal();

            tela.setVisible(true);
        });
    }
}