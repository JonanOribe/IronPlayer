package com.example.jonan.ironplayer;

import android.provider.BaseColumns;

public class Estructura_BBDD {

    private Estructura_BBDD(){

    }

    //public static class FeedEntry implements BaseColumns{

        public static final String TABLE_NAME="Tabla_registros";
        //public static final String ID_USUARIO="ID";
        public static final String NOMBRE_COLUMNA1="Usuario";
        public static final String NOMBRE_COLUMNA2="Password";
        public static final String NOMBRE_COLUMNA3="Email";

    //}
        private static final String TEXT_TYPE=" TEXT";
        private static final String COMMA_SEP=",";
        public static final String SQL_CREATE_ENTRIES=
                "CREATE TABLE "+Estructura_BBDD.TABLE_NAME+" ("+
                        Estructura_BBDD.NOMBRE_COLUMNA3+ TEXT_TYPE+" PRIMARY KEY, "+
                        Estructura_BBDD.NOMBRE_COLUMNA1+TEXT_TYPE+COMMA_SEP+
                        Estructura_BBDD.NOMBRE_COLUMNA2+TEXT_TYPE+" )";

        public static final String SQL_DELETE_ENTRIES=
                "DROP TABLE IF EXISTS "+Estructura_BBDD.TABLE_NAME;

}
