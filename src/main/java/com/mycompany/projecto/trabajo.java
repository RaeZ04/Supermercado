package com.mycompany.projecto;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.fasterxml.jackson.databind.ObjectMapper;

public class trabajo {
    public static void main(String[] args) throws IOException {
        File archivo = new File("Productos.xlsx");
        List<persona> personas = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        boolean terminar = false;

        // Mostrar todas las opciones

        while (!terminar) {
            System.out.println("\n¿Qué deseas hacer?\n1. Mostrar\n2. Registrar Compras\n3. Terminar");
            System.out.print("Elige la opción: ");
            int elegir = scanner.nextInt();
            System.out.println("");

            if (elegir == 1) {
                mostrarExcel(archivo);
            } else if (elegir == 2) {
                System.out.print("Cual es tu nombre: ");
                String nombre = scanner.next();

                boolean NumeroCorrecto = false;
                String dni = "";
                System.out.print("Introduce el DNI: ");
                dni = scanner.next();

                while (NumeroCorrecto == false) {
                    if (dni.length() != 9) {
                        System.out.println("El DNI no es correcto");
                        dni = scanner.next();
                    } else {
                        NumeroCorrecto = true;
                    }
                }

                persona personaExistente = buscarPersonaExistente(personas, nombre, dni);

                if (personaExistente == null) {
                    persona personaNueva = new persona(nombre, dni);
                    personas.add(personaNueva);
                    personaExistente = personaNueva;
                }

                boolean salircompra = true;

                while (salircompra == true) {
                    int id = 1;
                    System.out.print("Introduce el ID del producto que deseas comprar (0 para salir): ");
                    id = scanner.nextInt();

                    if (id != 0) {

                        registrarCompra(personaExistente, id, archivo);
                        System.out.println("Has comprado un producto.");
                    } else if (id == 0) {
                        salircompra = false;
                    }

                }

            } else if (elegir == 3) {
                System.out.println("Has salido del programa.");
                terminar = true;
                exportarCarritosAJSON(personas, "carrito_compra.json");
            } else {
                System.out.println(
                        "Opción no válida. Por favor, elige 1 para mostrar, 2 para registrar compras o 3 para terminar.");
            }
        }

        // Mostrar lo que ha comprado cada persona

        for (persona p : personas) {
            System.out.println(p.getName() + " con DNI " + p.getDNI() + " ha comprado:");

            for (Compra compra : p.getCompras()) {
                System.out.println("Producto ID: " + compra.getProducto().getId() +
                        ", Nombre: " + compra.getProducto().getNombre() +
                        ", Precio: " + compra.getProducto().getPrecio() +
                        ", Cantidad: " + compra.getCantidad());
            }

            System.out.println("Gasto Total: " + p.getGastoTotal());
        }
    }

    // Coger los nombres y precios del ID que has metido

    public static void registrarCompra(persona persona, int idProducto, File archivo) {
        try (FileInputStream fileInputStream = new FileInputStream(archivo);
                Workbook workBook = new XSSFWorkbook(fileInputStream)) {
            Sheet hoja = workBook.getSheetAt(0);

            for (Row fila : hoja) {
                if (fila.getCell(1).getCellType() == CellType.NUMERIC) {
                    int productoId = (int) fila.getCell(1).getNumericCellValue();
                    if (productoId == idProducto) {
                        String nombreProducto = fila.getCell(0).getStringCellValue();
                        double precioProducto = fila.getCell(3).getNumericCellValue();

                        Producto producto = new Producto(idProducto, nombreProducto, precioProducto);
                        persona.registrarCompra(producto);
                        return;
                    }
                }
            }
            System.out.println("No se encontró un producto con el ID proporcionado.");
        } catch (Exception e) {
            System.out.println("Ha fallado algo");
        }
    }

    // Mostrar todo el archivo del excel

    public static void mostrarExcel(File archivo) {
        try (FileInputStream fileInputStream = new FileInputStream(archivo);
                Workbook workBook = new XSSFWorkbook(fileInputStream)) {
            Sheet hoja = workBook.getSheetAt(0);

            for (Row fila : hoja) {
                for (Cell cell : fila) {
                    Cell valor = cell;

                    switch (cell.getCellType()) {
                        case NUMERIC:
                            int a = (int) cell.getNumericCellValue();
                            Integer s = a;
                            String devolver = s.toString();

                            int cellcol = cell.getColumnIndex();
                            if (cellcol == 1 || cellcol == 0) {
                                if (cell.getNumericCellValue() < 8) {
                                    valor = cell;
                                    System.out.print(devolver + "\t\t");
                                } else {
                                    valor = cell;
                                    System.out.print(devolver + "\t");
                                }
                            }
                            break;

                        case STRING:
                            int cellcolString = cell.getColumnIndex();
                            if (cellcolString == 1 || cellcolString == 0) {
                                if (cell.getStringCellValue().length() < 8) {
                                    valor = cell;
                                    System.out.print(valor + "\t\t");
                                } else {
                                    valor = cell;
                                    System.out.print(valor + "\t");
                                }
                            }
                            break;

                        default:
                            break;
                    }
                }
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("Ha fallado algo");
        }
    }

    // Comparar si las personas son iguales

    public static persona buscarPersonaExistente(List<persona> personas, String nombre, String dni) {
        for (persona p : personas) {
            if (p.getName().equals(nombre) && p.getDNI().equals(dni)) {
                return p;
            }
        }
        return null;
    }

    public static void exportarCarritosAJSON(List<persona> personas, String Carrito) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(Carrito), personas);
            System.out.println("Carritos exportados a JSON correctamente.");
        } catch (IOException e) {
            System.out.println("Error al exportar carritos a JSON: " + e.getMessage());
        }
    }

    
}
