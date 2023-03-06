package com.example.paraulogic;
/*
 * @author Pau Rosado Muñoz
 */
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.paraulogic.MESSAGE";
    // Conjunt de lletres amb les que es pot jugar
    UnsortedArraySet<Character> lletres;
    int[] idButton = new int[7];
    // Mapping de les paraules que hem introduit
    BSTmapping<String, Integer> paraules;
    TreeSet<String> diccionari;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Cream arbre on guardarem les paraules escrites
        paraules = new BSTmapping<>();
        // Carregam el diccionari
        llegirDiccionari();
        // Cercam un conjunt de 7 lletres fins que es pugui fer un tuti
        while (!cConjunt());
    }

    // Generador d'array de lletres
    public void gArray() {
        Random rnd = new Random();
        char l;
        for (int i = 0; i < 7; ) {
            l = (char) (rnd.nextInt(26) + 'A');
            if (lletres.add(l)) {
                i++;
            }
        }
    }

    // Funció que llegeix el diccionari i el almacena a un AVN
    public void llegirDiccionari() {
        // Objectes necesaris per llegir el arxiu
        InputStream is = getResources().openRawResource(R.raw.catala_filtrat);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String linia;
        // Cream l'abre VN
        diccionari = new TreeSet<>();
        try {
            linia = br.readLine();
            // Bucle per llegir tot l'arxiu
            while (linia != null) {
                // Afegim paraula al diccionari
                diccionari.add(linia);
//                System.out.println(linia);
                linia = br.readLine();
            }
            // Control d'errors
        } catch (IOException e) {
            System.out.println("Error {llegir diccionari} : " + e);
        }
    }

    // Mètode asigna lletres als botons
    public void assignarLletres() {
        Button boto;
        Iterator it = lletres.iterator();
        Character c;
        idButton[0] = R.id.BSE;
        idButton[1] = R.id.BSD;
        idButton[2] = R.id.BE;
        idButton[3] = R.id.BD;
        idButton[4] = R.id.BIE;
        idButton[5] = R.id.BID;
        idButton[6] = R.id.BC;
        for (int i = 0; i < 7; i++) {
            boto = findViewById(idButton[i]);
            c = (Character) it.next();
            boto.setText(c.toString());
        }
    }

    // Mètode per generar un conjunt de lletres
    public void gConjuntLletres() {
        lletres = new UnsortedArraySet<>(7);
        gArray();
        assignarLletres();
    }

    // Gestor events botons de lletres
    public void setLletra(View view) {
        // Objectes botó pitjat i casella escriptuta
        Button boto = findViewById(view.getId());
        TextView text = findViewById(R.id.Paraula);
        // Cream String amb contingut actual + el ja escrit
        String s = new StringBuilder().append(text.getText()).append(boto.getText()).toString();
        text.setText(s);
    }

    // Boto suprimir lletra
    public void supLletra(View view) {
        TextView text = findViewById(R.id.Paraula);
        // Cream String amb contingut actual + el ja escrit
        String s = text.getText().toString();
        if (s.length() > 0) {
            text.setText(s.substring(0, s.length() - 1));
        }
    }

    // Boto shuffle
    public void shuffle(View view) {
        Character [] auxl = new Character[7];
        Iterator it  = lletres.iterator();
        Random rnd = new Random();
        int j = 0;
        Character c;
        // Obtenim totes les lletres del nostre conjunt
        while (it.hasNext()) {
            auxl[j] = (Character) it.next();
            j++;
        }
        // Mesclam totes menys la centrall
        for (int i = 7 - 2; i > 0; i--) {
            j = rnd.nextInt(i + 1);
            c = auxl[i];
            auxl[i] = auxl[j];
            auxl[j] = c;
        }
        for (int i = 0; i < idButton.length; i++) {
            Button boto = findViewById(idButton[i]);
            boto.setText(auxl[i].toString());
        }
    }

    // Boto solucions
    public void mostSolucions(View view) {
        Intent intent = new Intent(this, Solucions.class);
        intent.putExtra(EXTRA_MESSAGE, gSolucions());
        startActivity(intent);
    }

    public void intParaula(View view) {
        // Agafam el boto central
        Button boto = findViewById(idButton[idButton.length - 1]);
        // Agafam la paraula escrita
        TextView text = findViewById(R.id.Paraula);
        // Comprovam si compté la lletra
        if (text.getText().toString().length() >= 3) {
            // Cas en el que sí te la lletra
            if (text.getText().toString().contains(boto.getText().toString())) {
                // Comprovam que la lletra es del diccionari
                if (diccionari.contains(text.getText().toString().toLowerCase())) {
                    // Cas en el que és al diccionari
                    Integer valor = paraules.get(text.getText().toString());
                    if (valor ==  null) {
                        paraules.put(text.getText().toString(), 1);
                    } else {
                        // Augmentam en 1 la quantitat de vegades que l'hem introduida
                        valor = valor + 1;
                        paraules.put(text.getText().toString(), valor);
                    }
                    // Actualitzam la llista de paraules escrites
                    Iterator it = paraules.iterator();
                    String s = "";
                    int cnt = 0;
                    BSTmapping.Pair p;
                    TextView respostes = findViewById(R.id.RespostesV);
                    while (it.hasNext()) {
                        cnt++;
                        p = (BSTmapping.Pair) it.next();
                        s += p.key.toString() + "(" + p.value + "), ";
                    }
                    System.out.println(s);
                    respostes.setText("Has trobat " + cnt + " paraules " + s);
                }
                // Cas en el que la paraula no es del diccionari
                else {
                    System.out.println("{introdueixParaula() -> No és una paraula vàlida}");
                    // Ho mostram per GUI
                    Context context = getApplicationContext();
                    CharSequence cs = "No és una paraula vàlida";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, cs, duration);
                    toast.show();
                }
            }
            // Cas en el que la paraula no compte la lletra central
            else {
                System.out.println("{introdueixParaula() -> No compté la lletra central}");
                // Ho mostam per GUI
                Context context = getApplicationContext();
                CharSequence cs = "No compté la lletra central";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, cs, duration);
                toast.show();
            }
        }
        // Cas en el que la paraula no té més de 3 lletres
        else {
            System.out.println("{introdueixParaula() -> No compté 3 o més lletres}");
            // Ho mostam per GUI
            Context context = getApplicationContext();
            CharSequence cs = "No compté 3 o més lletres";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, cs, duration);
            toast.show();
        }
        // Borram el que s'havia introduit
        text.setText("");
    }

    // Comprovar que en el conjunt hi ha una vocal
    private boolean cVocal() {
        Character [] vocals = {'A', 'E', 'I', 'O', 'U'};
        for (int i = 0; i < vocals.length; i++) {
            if (lletres.contains(vocals[i])) {
                return true;
            }
        }
        return false;
    }

    // Verificar que la paraula es pugui formar <=> lletres del nostre conjunt
    private boolean ppConjunt(String s) {
        boolean resultat = true;
        Iterator it = lletres.iterator();
        Character c = (Character) it.next();
        while (it.hasNext()) {
            c = (Character) it.next();
        }
        if (!s.toUpperCase().contains(c.toString())) {
            return false;
        }
        for (int i = 0; i < s.length() && resultat; i++) {
            if (lletres.contains(s.toUpperCase().charAt(i))) {
                resultat = true;
            } else {
                resultat = false;
            }
        }
        return resultat;
    }

    // Verificar que la paraula es tuti
    private boolean isTuti(String s) {
        int cnt = 0;
        Iterator it = lletres.iterator();
        while (it.hasNext()) {
            if (s.toUpperCase().contains(it.next().toString())) {
                cnt++;
            }
            if (cnt == 7) {
                return true;
            }
        }
        return false;
    }

    // Comprovar conjunt pugui formar un tuti
    private boolean cConjunt() {
        String s;
        boolean trobat = true;
        boolean vocal;
        Iterator<String> it = diccionari.iterator();
        // Generam un conjut de lletres
        gConjuntLletres();
        // Comprovam que hi hagui una vocal
        vocal = cVocal();
        // Bucle de totes les paraules
        while (it.hasNext() && vocal) {
            s = it.next();
            // Verificam que les lletres pertanyen al conjunt de lletres
            if (s.length() >= 7) {
                trobat = ppConjunt(s);
            }
            // Verificam que la paraula sigui Tuti
            if (trobat) {
                if (isTuti(s)) {
                    return true;
                }
            }
        }
    return false;
    }

    // Generador de solucions
    private String gSolucions() {
        String solucions = "";
        String paraula;
        Iterator itll = lletres.iterator();
        Iterator<String> itdic = diccionari.iterator();
        Character [] auxl = new Character[7];
        boolean trobat = false;
        // Guardam el conjunt generat
        for (int i = 0; itll.hasNext(); i++) {
            auxl[i] = (Character) itll.next();
        }
        // Bucle per recorrer les paraules
        while (itdic.hasNext()) {
            paraula = itdic.next();
            // Verificam que les lletres pertanyen al conjunt
            if (paraula.length() >= 3) {
                trobat = ppConjunt(paraula);
            }
            // Si es un tuti el posam vermell
            if (trobat) {
                if (isTuti(paraula)) {
                    solucions += "<font color = 'red'> " + paraula.toUpperCase() + "</font>, ";
                } else {
                    solucions += paraula.toUpperCase() + ", ";
                }
            }
        }
        return solucions;
    }

}