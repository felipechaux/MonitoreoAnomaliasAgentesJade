/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.agentes.db;

import co.agentes.Señal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe Chaux
 */
public class PersistenciaDB {

    public Connection conectar() {
        Connection con = null;
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            con = (java.sql.Connection) DriverManager.getConnection("jdbc:hsqldb:file:db\\agents", "admin", "1234");
            System.out.println("<--base conocimiento-->");
        } catch (Exception e) {
            System.out.println("error :" + e);
        }
        return con;
    }

    public Boolean consultaN(Señal registro) {
        Boolean result = null;
        Connection con = conectar();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("SELECT * FROM vibraciones where registro=? ");
            ps.setString(1, registro.getEntrada());
            rs = ps.executeQuery();

            if (rs.next()) {
                //         System.out.println("resultado bd-->" + rs.getString(1));

//                while (rs.next()) {
//
//                    // System.out.println("" + rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3));
//                    if (registro.getEntrada().equals(rs.getString(1))) {
//                        // System.out.println("coindice");
//                        result = true;
//                    } else {
//                        // System.out.println("no coincide");
//                        result = false;
//                    }
//                }
                result = true;
            } else {
                //System.out.println("resultado bd--> NOOO");
                result = false;
            }

        } catch (Exception e) {
            System.out.print("error" + e);
        } finally {
            // Cerrar todas las conexiones. Limpiar los recursos
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PersistenciaDB.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        }

        return result;
    }

    public Boolean consultaANM(Señal registro) {
        Boolean result = false;
        Connection con = conectar();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("SELECT * FROM vibraciones_anm where registro=? ");
            ps.setString(1, registro.getEntrada());
            rs = ps.executeQuery();

            if (rs.next()) {
                result = true;
            } else {
                //System.out.println("resultado bd--> NOOO");
                result = false;
            }

        } catch (Exception e) {
            System.out.print("error" + e);
        } finally {
            // Cerrar todas las conexiones. Limpiar los recursos
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PersistenciaDB.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        }

        return result;
    }


    public void insertN(Señal registro) {
        Connection con = conectar();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("INSERT INTO vibraciones (registro) VALUES(?)");

            ps.setString(1, registro.getEntrada());

            ps.executeUpdate();
            System.out.println("Conocimiento guardado");

        } catch (Exception e) {
            System.out.print("error" + e);
        } finally {
            // Cerrar todas las conexiones. Limpiar los recursos
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PersistenciaDB.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        }
    }

    public void insertANM(Señal registro) {
        Connection con = conectar();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("INSERT INTO vibraciones_anm (registro) VALUES(?)");

            ps.setString(1, registro.getEntrada());

            ps.executeUpdate();
            System.out.println("Conocimiento guardado");

        } catch (Exception e) {
            System.out.print("error" + e);
        } finally {
            // Cerrar todas las conexiones. Limpiar los recursos
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PersistenciaDB.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        }
    }

    public static void main(String args[]) throws SQLException {
        PersistenciaDB con = new PersistenciaDB();
        con.consultaN(null);
    }
}
