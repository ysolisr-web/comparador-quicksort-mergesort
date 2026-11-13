package logica;

import modelo.ResultadoMetricas;

public class AlgoritmosOrdenamiento {

    private static long llamadasRecursivas;
    private static long comparaciones;

    // ==================== QUICK SORT ====================
    public static ResultadoMetricas ejecutarQuickSort(int[] arregloOriginal) {
        int[] copia = arregloOriginal.clone();
        llamadasRecursivas = 0;
        comparaciones = 0;

        long inicio = System.nanoTime();
        if (copia.length > 1) {
            quickSortRecursivo(copia, 0, copia.length - 1);
        }
        long fin = System.nanoTime();

        double tiempoMs = (fin - inicio) / 1_000_000.0;
        // Quick Sort ordena in-place; su memoria auxiliar corresponde a la pila de recursión (~O(log n))
        long profundidadAprox = (copia.length > 0) ? (long) (Math.log(copia.length) / Math.log(2)) + 1 : 0;
        long memoriaAuxBytes = profundidadAprox * 24L;

        return new ResultadoMetricas(
                "Quick Sort",
                copia.length,
                tiempoMs,
                llamadasRecursivas,
                comparaciones,
                memoriaAuxBytes,
                estaOrdenadoAsc(copia)
        );
    }

    private static void quickSortRecursivo(int[] a, int lo, int hi) {
        llamadasRecursivas++;
        int i = lo;
        int j = hi;
        int pivote = a[(lo + hi) / 2];

        while (i <= j) {
            while (a[i] < pivote) {
                comparaciones++;
                i++;
            }
            comparaciones++;

            while (a[j] > pivote) {
                comparaciones++;
                j--;
            }
            comparaciones++;

            if (i <= j) {
                int temp = a[i];
                a[i] = a[j];
                a[j] = temp;
                i++;
                j--;
            }
        }

        if (lo < j) {
            quickSortRecursivo(a, lo, j);
        }
        if (i < hi) {
            quickSortRecursivo(a, i, hi);
        }
    }

    // ==================== MERGE SORT ====================
    public static ResultadoMetricas ejecutarMergeSort(int[] arregloOriginal) {
        int[] copia = arregloOriginal.clone();
        llamadasRecursivas = 0;
        comparaciones = 0;

        long inicio = System.nanoTime();
        int[] buffer = new int[copia.length];
        if (copia.length > 1) {
            mergeSortRecursivo(copia, buffer, 0, copia.length);
        }
        long fin = System.nanoTime();

        double tiempoMs = (fin - inicio) / 1_000_000.0;
        // Memoria auxiliar: arreglo buffer de n enteros (n * 4 bytes)
        long memoriaAuxBytes = (long) copia.length * Integer.BYTES;

        return new ResultadoMetricas(
                "Merge Sort",
                copia.length,
                tiempoMs,
                llamadasRecursivas,
                comparaciones,
                memoriaAuxBytes,
                estaOrdenadoAsc(copia)
        );
    }

    private static void mergeSortRecursivo(int[] a, int[] buffer, int lo, int hi) {
        llamadasRecursivas++;
        if (hi - lo < 2) {
            return;
        }

        int mid = (lo + hi) / 2;
        mergeSortRecursivo(a, buffer, lo, mid);
        mergeSortRecursivo(a, buffer, mid, hi);

        int i = lo, j = mid, k = lo;
        while (i < mid && j < hi) {
            comparaciones++;
            if (a[i] <= a[j]) {
                buffer[k++] = a[i++];
            } else {
                buffer[k++] = a[j++];
            }
        }

        while (i < mid) {
            buffer[k++] = a[i++];
        }
        while (j < hi) {
            buffer[k++] = a[j++];
        }

        System.arraycopy(buffer, lo, a, lo, hi - lo);
    }

    // ==================== UTILIDADES ====================
    public static boolean estaOrdenadoAsc(int[] a) {
        for (int i = 1; i < a.length; i++) {
            if (a[i - 1] > a[i]) {
                return false;
            }
        }
        return true;
    }

    public static int[] generarArregloAleatorio(int tamano, int maxValor) {
        int[] datos = new int[tamano];
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < tamano; i++) {
            datos[i] = random.nextInt(maxValor);
        }
        return datos;
    }
}
