package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import logica.AlgoritmosOrdenamiento;
import modelo.ResultadoMetricas;

public class VentanaComparador extends JFrame {

    private final JComboBox<String> comboTamano;
    private final JButton btnEjecutar;
    private final JButton btnLimpiar;
    private final DefaultTableModel modeloTabla;
    private final JTextArea areaAnalisis;

    public VentanaComparador() {
        setTitle("Ejercicio 23 - Comparador de Rendimiento: Quick Sort vs. Merge Sort");
        setSize(880, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ==================== PANEL SUPERIOR (CONTROLES) ====================
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Configuración de Prueba"));

        panelSuperior.add(new JLabel("Tamaño del arreglo (n):"));
        String[] tamanos = {"1000", "5000", "10000", "50000", "100000", "250000"};
        comboTamano = new JComboBox<>(tamanos);
        comboTamano.setEditable(true);
        comboTamano.setSelectedItem("50000");
        panelSuperior.add(comboTamano);

        btnEjecutar = new JButton("Ejecutar Comparación");
        btnEjecutar.setBackground(new Color(33, 115, 70));
        btnEjecutar.setForeground(Color.WHITE);
        btnEjecutar.setFocusPainted(false);

        btnLimpiar = new JButton("Limpiar Resultados");

        panelSuperior.add(btnEjecutar);
        panelSuperior.add(btnLimpiar);

        add(panelSuperior, BorderLayout.NORTH);

        // ==================== PANEL CENTRAL (TABLA DE MÉTRICAS) ====================
        String[] columnas = {
            "Algoritmo",
            "Tamaño (n)",
            "Tiempo (ms)",
            "Llamadas Recursivas",
            "Comparaciones",
            "Memoria Auxiliar (KB)",
            "¿Ordenado?"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tablaResultados = new JTable(modeloTabla);
        tablaResultados.setRowHeight(26);
        tablaResultados.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));

        JScrollPane scrollTabla = new JScrollPane(tablaResultados);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Métricas de Ejecución"));
        add(scrollTabla, BorderLayout.CENTER);

        // ==================== PANEL INFERIOR (ANÁLISIS COMPARATIVO) ====================
        areaAnalisis = new JTextArea(7, 60);
        areaAnalisis.setEditable(false);
        areaAnalisis.setLineWrap(true);
        areaAnalisis.setWrapStyleWord(true);
        areaAnalisis.setFont(new Font("SansSerif", Font.PLAIN, 13));
        areaAnalisis.setText("Seleccione un tamaño de arreglo y presione 'Ejecutar Comparación' para evaluar ambos algoritmos sobre exactamente los mismos datos aleatorios.");

        JScrollPane scrollAnalisis = new JScrollPane(areaAnalisis);
        scrollAnalisis.setBorder(BorderFactory.createTitledBorder("Análisis Técnico y Comparación"));
        add(scrollAnalisis, BorderLayout.SOUTH);

        // ==================== EVENTOS ====================
        btnEjecutar.addActionListener(e -> ejecutarBenchmark());
        btnLimpiar.addActionListener(e -> {
            modeloTabla.setRowCount(0);
            areaAnalisis.setText("Resultados limpiados. Listo para una nueva ejecución.");
        });
    }

    private void ejecutarBenchmark() {
        try {
            Object seleccion = comboTamano.getEditor().getItem();
            int n = Integer.parseInt(seleccion.toString().trim());

            if (n < 10 || n > 2_000_000) {
                JOptionPane.showMessageDialog(this,
                        "Ingrese un tamaño válido entre 10 y 2,000,000 elementos.",
                        "Tamaño fuera de rango",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int[] datosBase = AlgoritmosOrdenamiento.generarArregloAleatorio(n, n * 10);

            ResultadoMetricas resQuick = AlgoritmosOrdenamiento.ejecutarQuickSort(datosBase);
            ResultadoMetricas resMerge = AlgoritmosOrdenamiento.ejecutarMergeSort(datosBase);

            agregarFila(resQuick);
            agregarFila(resMerge);
            mostrarAnalisis(resQuick, resMerge);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Por favor ingrese un número entero válido para el tamaño del arreglo.",
                    "Entrada inválida",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarFila(ResultadoMetricas r) {
        modeloTabla.addRow(new Object[]{
            r.getAlgoritmo(),
            String.format("%,d", r.getTamano()),
            String.format("%.4f", r.getTiempoMs()),
            String.format("%,d", r.getLlamadasRecursivas()),
            String.format("%,d", r.getComparaciones()),
            String.format("%.2f KB", r.getMemoriaAuxiliarBytes() / 1024.0),
            r.isOrdenadoCorrectamente() ? "Sí" : "No"
        });
    }

    private void mostrarAnalisis(ResultadoMetricas q, ResultadoMetricas m) {
        String masRapido = (q.getTiempoMs() < m.getTiempoMs()) ? "Quick Sort" : "Merge Sort";
        double diferenciaTiempo = Math.abs(q.getTiempoMs() - m.getTiempoMs());

        String resumen = String.format(
                "Resultado para n = %,d elementos:\n"
                + "• Velocidad: %s fue más rápido por %.4f ms (Quick Sort: %.4f ms vs. Merge Sort: %.4f ms).\n"
                + "• Memoria auxiliar: Quick Sort operó in-place usando ~%.2f KB en pila de recursión O(log n), "
                + "mientras Merge Sort requirió %.2f KB para su arreglo auxiliar O(n).\n"
                + "• Recursión y comparaciones: Quick Sort realizó %,d llamadas y %,d comparaciones; "
                + "Merge Sort realizó %,d llamadas y %,d comparaciones.",
                q.getTamano(),
                masRapido,
                diferenciaTiempo,
                q.getTiempoMs(),
                m.getTiempoMs(),
                q.getMemoriaAuxiliarBytes() / 1024.0,
                m.getMemoriaAuxiliarBytes() / 1024.0,
                q.getLlamadasRecursivas(),
                q.getComparaciones(),
                m.getLlamadasRecursivas(),
                m.getComparaciones()
        );

        areaAnalisis.setText(resumen);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaComparador().setVisible(true);
        });
    }
}
