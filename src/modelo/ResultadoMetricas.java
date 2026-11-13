package modelo;

public class ResultadoMetricas {
    private final String algoritmo;
    private final int tamano;
    private final double tiempoMs;
    private final long llamadasRecursivas;
    private final long comparaciones;
    private final long memoriaAuxiliarBytes;
    private final boolean ordenadoCorrectamente;

    public ResultadoMetricas(String algoritmo, int tamano, double tiempoMs,
                             long llamadasRecursivas, long comparaciones,
                             long memoriaAuxiliarBytes, boolean ordenadoCorrectamente) {
        this.algoritmo = algoritmo;
        this.tamano = tamano;
        this.tiempoMs = tiempoMs;
        this.llamadasRecursivas = llamadasRecursivas;
        this.comparaciones = comparaciones;
        this.memoriaAuxiliarBytes = memoriaAuxiliarBytes;
        this.ordenadoCorrectamente = ordenadoCorrectamente;
    }

    public String getAlgoritmo() { return algoritmo; }
    public int getTamano() { return tamano; }
    public double getTiempoMs() { return tiempoMs; }
    public long getLlamadasRecursivas() { return llamadasRecursivas; }
    public long getComparaciones() { return comparaciones; }
    public long getMemoriaAuxiliarBytes() { return memoriaAuxiliarBytes; }
    public boolean isOrdenadoCorrectamente() { return ordenadoCorrectamente; }
}