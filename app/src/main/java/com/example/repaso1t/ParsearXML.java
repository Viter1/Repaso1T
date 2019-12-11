package com.example.repaso1t;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ParsearXML {
    Servicio s  = null;
    private static final String ns = null;

    private static final String ETIQUETA_SERVICELIST = "serviceList";
    private static final String ETIQUETA_SERVICE = "service";
    private static final String ETIQUETA_BASICDATA = "basicData";
    private static final String ETIQUETA_TITLE = "title";
    private static final String ETIQUETA_BODY = "body";
    private static final String ETIQUETA_URL = "url";

    public List<Servicio> parsear(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return leerContactos(parser);
        } finally {
            in.close();
        }
    }

    private List<Servicio> leerContactos(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        List<Servicio>  lista_contactos = new ArrayList<Servicio>();

        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_SERVICELIST);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String nombreEtiqueta = parser.getName();
            // Buscar etiqueta <contacto>
            if (nombreEtiqueta.equals(ETIQUETA_SERVICE)) {
                lista_contactos.add(leerContacto(parser));
            } else {
                saltarEtiqueta(parser);
            }
        }
        return lista_contactos;
    }

    private Servicio leerContacto(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_SERVICE);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals(ETIQUETA_BASICDATA)){
                leerBasicData(parser);
            }else{
                saltarEtiqueta(parser);
            }


        }
        return s;

    }

    private Servicio leerBasicData(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_BASICDATA);
        String title = null;
        String body = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals(ETIQUETA_TITLE)){
               title =  leerTitle(parser);

            }else if (name.equals(ETIQUETA_BODY)){
                body = leerBody(parser);
            }else{
                saltarEtiqueta(parser);
            }


        }
        return s = new Servicio(title,body);

    }





    private String leerNombre(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_BASICDATA);
        String nombre = obtenerTexto(parser);
        parser.require(XmlPullParser.END_TAG, ns, ETIQUETA_BASICDATA);
        return nombre;
    }

    private String leerTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_TITLE);
        String apellido = obtenerTexto(parser);
        parser.require(XmlPullParser.END_TAG, ns, ETIQUETA_TITLE);
        return apellido;
    }

    private String leerBody(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_BODY);
        String email = obtenerTexto(parser);
        parser.require(XmlPullParser.END_TAG, ns, ETIQUETA_BODY);
        return email;
    }

    private String obtenerTexto(XmlPullParser parser) throws IOException, XmlPullParserException {
        String resultado = "";
        if (parser.next() == XmlPullParser.TEXT) {
            resultado = parser.getText();
            parser.nextTag();
        }
        return resultado;
    }


    private void saltarEtiqueta(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
