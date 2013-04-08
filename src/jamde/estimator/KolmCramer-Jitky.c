% Cramer alfa, yobecněný cramer, místo druhé mocniny je p / q mocnina
CramerAlfaIntEstimator::CramerAlfaIntEstimator(int Parametr_c, int Parametr_j) {
    this->Parametr_c = Parametr_c;
    this->Parametr_j = Parametr_j;
    QString parc;
    parc.setNum(Parametr_c);
    QString parj;
    parj.setNum(Parametr_j);
    this->typeOfEstimator = "crameralfaint " + parj + parc;
}

void CramerAlfaIntEstimator::Minimalize(Distribution *d1, double *data, int SIZE_OF_SAMPLE) {
    string type = d1->getType();
    if (type == "Weibull") {
        Minimalize3D_2(d1, data, SIZE_OF_SAMPLE);
        return;
    } else {
        Minimalize2D_2(d1, data, SIZE_OF_SAMPLE);
        return;
    }

}

double CramerAlfaIntEstimator::CountDistance(Distribution *d1, double *data, int SIZE_OF_SAMPLE) {
    int parj = this->Parametr_j;
    int parc = this->Parametr_c;
    double d = 0.0;
    double y;
    double a;
    double b;

    /* for (int i = 0; i< SIZE_OF_SAMPLE; i++)
     {
         y = d1->getFunctionValue(data[i]);
         d = d+pow(y-(i*1.0)/SIZE_OF_SAMPLE ,3) - pow(y-((i+1)*1)/SIZE_OF_SAMPLE,3);
     }
     d = d/3;
     return d;  */

    for (int i = 0; i < SIZE_OF_SAMPLE; i++) {
        y = d1->getFunctionValue(data[i]);

        if (y - ((i)*1.0) / SIZE_OF_SAMPLE < 0) {
            a = -1.0;
        } else {
            a = 1.0;
        }
        if (y - ((i + 1)*1.0) / SIZE_OF_SAMPLE < 0) {
            b = -1.0;
        } else {
            b = 1.0;
        }

        d = d + a * pow(abs(y - ((i)*1.0) / SIZE_OF_SAMPLE), (double) parc / parj + 1.0) - b * pow(abs(y - ((i + 1)*1.0) / SIZE_OF_SAMPLE), (double) parc / parj + 1.0);
    }
    d = d / (parc / parj + 1.0);
    return d;
}

% Kolmogorov Cramer estimator parametr m je volen pevne

KolmCramEstimator::KolmCramEstimator(int Parametr_c, int Parametr_j, int Parametr_m) {
    this->Parametr_c = Parametr_c;
    this->Parametr_j = Parametr_j;
    this->Parametr_m = Parametr_m;
    QString parc;
    parc.setNum(Parametr_c);
    QString parj;
    parj.setNum(Parametr_j);
    QString parm;
    parm.setNum(Parametr_m);
    this->typeOfEstimator = "kolmcram " + parc + parj + parm;
}

void KolmCramEstimator::Minimalize(Distribution *d1, double *data, int SIZE_OF_SAMPLE) {
    string type = d1->getType();
    if (type == "Weibull") {
        Minimalize3D_2(d1, data, SIZE_OF_SAMPLE);
        return;
    } else {


        Minimalize2D_2(d1, data, SIZE_OF_SAMPLE);
        return;
    }

}

double KolmCramEstimator::CountDistance(Distribution *d1, double *data, int SIZE_OF_SAMPLE) {
    int parj = this->Parametr_j;
    int parc = this->Parametr_c;
    double y;
    double d[2 * SIZE_OF_SAMPLE];
    double e = 0.0;
    int m = this->Parametr_m;
    double h = Math::Max2(2 * SIZE_OF_SAMPLE - m, 0);

    // SortArray dd;



    /* QFile filePP(adresar+"PP.txt");
     filePP.open(QIODevice::Append);
     QTextStream outPP(&filePP);*/

    for (int i = 0; i < SIZE_OF_SAMPLE; i++) {
        y = d1->getFunctionValue(data[i]);
        d[i] = pow(abs(((i + 1)*1.0) / SIZE_OF_SAMPLE - y), (double) parc / parj);
        d[2 * SIZE_OF_SAMPLE - 1 - i] = pow(abs(((i)*1.0) / SIZE_OF_SAMPLE - y), (double) parc / parj);

        /*    outPP << y << " " << ((i+1)*1.0)/SIZE_OF_SAMPLE << "    " << ((i)*1.0)/SIZE_OF_SAMPLE;
            outPP << endl; */

    }

    // SortArray::Sort(d,2*SIZE_OF_SAMPLE);

    /* double soucetPP = 0.0;
       for(int jjj = 0; jjj< 2*SIZE_OF_SAMPLE; jjj++)

     { soucetPP=d[jjj]+d[2*SIZE_OF_SAMPLE-1-jjj];

           outPP << soucetPP << endl;
       soucetPP=0.0;
       }
     */

    // filePP.close();



    int i, j, l, r;
    double x, w;
    int size = 2 * SIZE_OF_SAMPLE;
    double* array = d;

    Stack z = Stack(size / 2 + 1); // inicializace zasobniku
    z.Insert(0, size - 1);
    do {
        z.Remove(&l, &r);
        do {
            i = l;
            j = r;
            x = array[(l + r) / 2];
            do {
                while (array[i] < x) i++;
                while (array[j] > x) j--;
                if (i <= j) {
                    w = array[i];
                    array[i] = array[j];
                    array[j] = w;
                    i++;
                    j--;
                }
            } while (i < j);
            if (i < r) z.Insert(i, r);
            r = j;
        } while (l <= r);
    }    while (!z.Empty());

    array = NULL;



    //  Vypisuje součty poli do souboru
    /*    QFile fileP(adresar+"P.txt");
        fileP.open(QIODevice::Append);
        QTextStream outP(&fileP);
        double soucetP = 0;
          for(int jjj = 0; jjj< size; jjj++)
              //outP << d[jjj] << "  ";
                soucetP=soucetP+d[jjj];
          outP << soucetP << endl;

          fileP.close();
     */



    for (int i = 2 * SIZE_OF_SAMPLE - 1; i >= h; i--) {
        e = e + d[i];
    }

    e = e / m;

    return e;
}


// KOLMCRAM S parametrem m rovným 2* samplesize na beta

KolmCramPoverEstimator::KolmCramPoverEstimator(int Parametr_c, int Parametr_j, double Parametr_b) {
    this->Parametr_c = Parametr_c;
    this->Parametr_j = Parametr_j;
    this->Parametr_b = Parametr_b;
    QString parc;
    parc.setNum(Parametr_c);
    QString parj;
    parj.setNum(Parametr_j);
    QString parb;
    parb.setNum(Parametr_b);
    this->typeOfEstimator = "kolmcrampover " + parc + parj + parb;
}

void KolmCramPoverEstimator::Minimalize(Distribution *d1, double *data, int SIZE_OF_SAMPLE) {
    string type = d1->getType();
    if (type == "Weibull") {
        Minimalize3D_2(d1, data, SIZE_OF_SAMPLE);
        return;
    } else {


        Minimalize2D_2(d1, data, SIZE_OF_SAMPLE);
        return;
    }

}

double KolmCramPoverEstimator::CountDistance(Distribution *d1, double *data, int SIZE_OF_SAMPLE) {
    int parj = this->Parametr_j;
    int parc = this->Parametr_c;
    double y;
    double d[2 * SIZE_OF_SAMPLE];
    double e = 0.0;
    double b = this->Parametr_b;
    double m = 2 * pow(SIZE_OF_SAMPLE, b);
    double h = Math::Max2(2 * SIZE_OF_SAMPLE - m, 0);
    // SortArray dd;


    /* QFile filePP(adresar+"PP.txt");
     filePP.open(QIODevice::Append);
     QTextStream outPP(&filePP);*/

    for (int i = 0; i < SIZE_OF_SAMPLE; i++) {
        y = d1->getFunctionValue(data[i]);
        d[i] = pow(abs(((i + 1)*1.0) / SIZE_OF_SAMPLE - y), (double) parc / parj);
        d[2 * SIZE_OF_SAMPLE - 1 - i] = pow(abs(((i)*1.0) / SIZE_OF_SAMPLE - y), (double) parc / parj);

        /*    outPP << y << " " << ((i+1)*1.0)/SIZE_OF_SAMPLE << "    " << ((i)*1.0)/SIZE_OF_SAMPLE;
            outPP << endl; */

    }

    // SortArray::Sort(d,2*SIZE_OF_SAMPLE);

    /* double soucetPP = 0.0;
       for(int jjj = 0; jjj< 2*SIZE_OF_SAMPLE; jjj++)

     { soucetPP=d[jjj]+d[2*SIZE_OF_SAMPLE-1-jjj];

           outPP << soucetPP << endl;
       soucetPP=0.0;
       }
     */

    // filePP.close();



    int i, j, l, r;
    double x, w;
    int size = 2 * SIZE_OF_SAMPLE;
    double* array = d;

    Stack z = Stack(size / 2 + 1); // inicializace zasobniku
    z.Insert(0, size - 1);
    do {
        z.Remove(&l, &r);
        do {
            i = l;
            j = r;
            x = array[(l + r) / 2];
            do {
                while (array[i] < x) i++;
                while (array[j] > x) j--;
                if (i <= j) {
                    w = array[i];
                    array[i] = array[j];
                    array[j] = w;
                    i++;
                    j--;
                }
            } while (i < j);
            if (i < r) z.Insert(i, r);
            r = j;
        } while (l <= r);
    }    while (!z.Empty());

    array = NULL;



    //  Vypisuje součty poli do souboru
    /*    QFile fileP(adresar+"P.txt");
        fileP.open(QIODevice::Append);
        QTextStream outP(&fileP);
        double soucetP = 0;
          for(int jjj = 0; jjj< size; jjj++)
              //outP << d[jjj] << "  ";
                soucetP=soucetP+d[jjj];
          outP << soucetP << endl;

          fileP.close();
     */



    for (int i = 2 * SIZE_OF_SAMPLE - 1; i >= h; i--) {
        e = e + d[i];
    }

    e = e / m;

    return e;
}

/* MODIFIKOVANÝ KOLMCRAM NÁHODNÁ VOLBA INTERVALŮ M PEVNÉ (VÁCLAVŮV NÁVRH) PRO VŠECHNY TYPY (TO JE KC2, KC3, KC4)
 */
KolmCramRandEstimator::KolmCramRandEstimator(int Parametr_c, int Parametr_j, int Parametr_m, int Parametr_k, int Parametr_exp, double *vyber) {
    this->Parametr_c = Parametr_c;
    this->Parametr_j = Parametr_j;
    this->Parametr_m = Parametr_m;
    this->Parametr_k = Parametr_k;
    this->Parametr_exp = Parametr_exp;
    this->vyber = vyber;
    QString parc;
    parc.setNum(Parametr_c);
    QString parj;
    parj.setNum(Parametr_j);
    QString parm;
    parm.setNum(Parametr_m);
    QString park;
    park.setNum(Parametr_k);
    QString parexp;
    parexp.setNum(Parametr_exp);
    this->typeOfEstimator = "kolmcramrand " + parc + parj + parm + park + parexp;
}

void KolmCramRandEstimator::Minimalize(Distribution *d1, double *data, int SIZE_OF_SAMPLE) {
    string type = d1->getType();
    if (type == "Weibull") {
        Minimalize3D_2(d1, data, SIZE_OF_SAMPLE);
        return;
    } else {


        Minimalize2D_2(d1, data, SIZE_OF_SAMPLE);
        return;
    }

}

double KolmCramRandEstimator::CountDistance(Distribution *d1, double *data, int SIZE_OF_SAMPLE) {
    int parj = this->Parametr_j;
    int parc = this->Parametr_c;
    double y;
    double d[2 * SIZE_OF_SAMPLE];
    double e = 0.0;
    int m = this->Parametr_m;
    int k = this->Parametr_k;
    int exp = this->Parametr_exp;
    int h = Math::Min2(m - 2, 2 * SIZE_OF_SAMPLE - 1);

    // SortArray dd;


    /*QFile filePP(adresar+"PP.txt");
    filePP.open(QIODevice::Append);
    QTextStream outPP(&filePP);*/

    for (int i = 0; i < SIZE_OF_SAMPLE; i++) {
        y = d1->getFunctionValue(data[i]);
        d[i] = pow(abs(((i + 1)*1.0) / SIZE_OF_SAMPLE - y), (double) parc / parj);
        d[2 * SIZE_OF_SAMPLE - 1 - i] = pow(abs(((i)*1.0) / SIZE_OF_SAMPLE - y), (double) parc / parj);

        /*    outPP << y << " " << ((i+1)*1.0)/SIZE_OF_SAMPLE << "    " << ((i)*1.0)/SIZE_OF_SAMPLE;
            outPP << endl; */

    }


    int i, j, l, r;
    double x, w;
    int size = 2 * SIZE_OF_SAMPLE;
    double* array = d;

    Stack z = Stack(size / 2 + 1); // inicializace zasobniku
    z.Insert(0, size - 1);
    do {
        z.Remove(&l, &r);
        do {
            i = l;
            j = r;
            x = array[(l + r) / 2];
            do {
                while (array[i] < x) i++;
                while (array[j] > x) j--;
                if (i <= j) {
                    w = array[i];
                    array[i] = array[j];
                    array[j] = w;
                    i++;
                    j--;
                }
            } while (i < j);
            if (i < r) z.Insert(i, r);
            r = j;
        } while (l <= r);
    }    while (!z.Empty());

    array = NULL;



    //  Vypisuje součty poli do souboru
    /*    QFile fileP(adresar+"P.txt");
        fileP.open(QIODevice::Append);
        QTextStream outP(&fileP);
        double soucetP = 0;
          for(int jjj = 0; jjj< size; jjj++)
              //outP << d[jjj] << "  ";
                soucetP=soucetP+d[jjj];
          outP << soucetP << endl;

          fileP.close();
     */


    for (int i = 0; i <= h; i++) {
        int U = floor(vyber[i]);
        e = e + pow(2 * SIZE_OF_SAMPLE - U, exp) * d[(int) U];
    }

    e = e / pow((double) m, exp + 1);
    e = e + d[2 * SIZE_OF_SAMPLE - 1] / (k * m);

    return e;
}

/* MODIFIKOVANÝ KOLMCRAM NÁHODNÁ VOLBA INTERVALŮ M ZÁVISLÉ NA N (VÁCLAVŮV NÁVRH) PRO VŠECHNY TYPY (TO JE KC2, KC3, KC4)
 */
KolmCramPRandEstimator::KolmCramPRandEstimator(int Parametr_c, int Parametr_j, double Parametr_b, int Parametr_k, int Parametr_exp, double *vyber) {
    this->Parametr_c = Parametr_c;
    this->Parametr_j = Parametr_j;
    this->Parametr_b = Parametr_b;
    this->Parametr_k = Parametr_k;
    this->Parametr_exp = Parametr_exp;
    this->vyber = vyber;
    QString parc;
    parc.setNum(Parametr_c);
    QString parj;
    parj.setNum(Parametr_j);
    QString parb;
    parb.setNum(Parametr_b);
    QString park;
    park.setNum(Parametr_k);
    QString parexp;
    parexp.setNum(Parametr_exp);
    this->typeOfEstimator = "kolmcramprand " + parc + parj + parb + park + parexp;
}

void KolmCramPRandEstimator::Minimalize(Distribution *d1, double *data, int SIZE_OF_SAMPLE) {
    string type = d1->getType();
    if (type == "Weibull") {
        Minimalize3D_2(d1, data, SIZE_OF_SAMPLE);
        return;
    } else {


        Minimalize2D_2(d1, data, SIZE_OF_SAMPLE);
        return;
    }

}

double KolmCramPRandEstimator::CountDistance(Distribution *d1, double *data, int SIZE_OF_SAMPLE) {
    int parj = this->Parametr_j;
    int parc = this->Parametr_c;
    double y;
    double d[2 * SIZE_OF_SAMPLE];
    double e = 0.0;
    double b = this->Parametr_b;
    double m = 2 * pow(SIZE_OF_SAMPLE, b);
    int k = this->Parametr_k;
    int exp = this->Parametr_exp;
    double h = Math::Min2(m - 2, 2 * SIZE_OF_SAMPLE - 1);

    // SortArray dd;


    /* QFile filePP(adresar+"PP.txt");
     filePP.open(QIODevice::Append);
     QTextStream outPP(&filePP);*/

    for (int i = 0; i < SIZE_OF_SAMPLE; i++) {
        y = d1->getFunctionValue(data[i]);
        d[i] = pow(abs(((i + 1)*1.0) / SIZE_OF_SAMPLE - y), (double) parc / parj);
        d[2 * SIZE_OF_SAMPLE - 1 - i] = pow(abs(((i)*1.0) / SIZE_OF_SAMPLE - y), (double) parc / parj);

        /*    outPP << y << " " << ((i+1)*1.0)/SIZE_OF_SAMPLE << "    " << ((i)*1.0)/SIZE_OF_SAMPLE;
            outPP << endl; */

    }


    int i, j, l, r;
    double x, w;
    int size = 2 * SIZE_OF_SAMPLE;
    double* array = d;

    Stack z = Stack(size / 2 + 1); // inicializace zasobniku
    z.Insert(0, size - 1);
    do {
        z.Remove(&l, &r);
        do {
            i = l;
            j = r;
            x = array[(l + r) / 2];
            do {
                while (array[i] < x) i++;
                while (array[j] > x) j--;
                if (i <= j) {
                    w = array[i];
                    array[i] = array[j];
                    array[j] = w;
                    i++;
                    j--;
                }
            } while (i < j);
            if (i < r) z.Insert(i, r);
            r = j;
        } while (l <= r);
    }    while (!z.Empty());

    array = NULL;



    //  Vypisuje součty poli do souboru
    /*    QFile fileP(adresar+"P.txt");
        fileP.open(QIODevice::Append);
        QTextStream outP(&fileP);
        double soucetP = 0;
          for(int jjj = 0; jjj< size; jjj++)
              //outP << d[jjj] << "  ";
                soucetP=soucetP+d[jjj];
          outP << soucetP << endl;

          fileP.close();
     */



    for (int i = 0; i <= h; i++) {
        int U = floor(vyber[i]);
        e = e + pow(2 * SIZE_OF_SAMPLE - U, exp) * d[(int) U];
    }

    e = e / pow((double) m, exp + 1);
    e = e + d[2 * SIZE_OF_SAMPLE - 1] / (k * m);

    return e;
}

/* MODIFIKOVANÝ KOLMCRAM NÁHODNÁ VOLBA INTERVALŮ M ZÁVISLÉ NA N  jako procenta z 2*SIZE_OF_SAMPLE (VÁCLAVŮV NÁVRH) PRO VŠECHNY TYPY (TO JE KC2, KC3, KC4)
   KCFR-m=fraction of 2 SIZE_OF_SAMPLE  
 */

KolmCramFRandEstimator::KolmCramFRandEstimator(int Parametr_c, int Parametr_j, double Parametr_f, int Parametr_k, int Parametr_exp, double *vyber) {
    this->Parametr_c = Parametr_c;
    this->Parametr_j = Parametr_j;
    this->Parametr_f = Parametr_f;
    this->Parametr_k = Parametr_k;
    this->Parametr_exp = Parametr_exp;
    this->vyber = vyber;
    QString parc;
    parc.setNum(Parametr_c);
    QString parj;
    parj.setNum(Parametr_j);
    QString parf;
    parf.setNum(Parametr_f);
    QString park;
    park.setNum(Parametr_k);
    QString parexp;
    parexp.setNum(Parametr_exp);
    this->typeOfEstimator = "kolmcramfrand " + parc + parj + parf + park + parexp;
}

void KolmCramFRandEstimator::Minimalize(Distribution *d1, double *data, int SIZE_OF_SAMPLE) {
    string type = d1->getType();
    if (type == "Weibull") {
        Minimalize3D_2(d1, data, SIZE_OF_SAMPLE);
        return;
    } else {


        Minimalize2D_2(d1, data, SIZE_OF_SAMPLE);
        return;
    }

}

double KolmCramFRandEstimator::CountDistance(Distribution *d1, double *data, int SIZE_OF_SAMPLE) {
    int parj = this->Parametr_j;
    int parc = this->Parametr_c;
    double y;
    double d[2 * SIZE_OF_SAMPLE];
    double e = 0.0;
    double f = this->Parametr_f;
    double m = 2 * SIZE_OF_SAMPLE*f;
    int k = this->Parametr_k;
    int exp = this->Parametr_exp;
    double h = Math::Min2(m - 2, 2 * SIZE_OF_SAMPLE - 1);

    // SortArray dd;


    /* QFile filePP(adresar+"PP.txt");
     filePP.open(QIODevice::Append);
     QTextStream outPP(&filePP);*/

    for (int i = 0; i < SIZE_OF_SAMPLE; i++) {
        y = d1->getFunctionValue(data[i]);
        d[i] = pow(abs(((i + 1)*1.0) / SIZE_OF_SAMPLE - y), (double) parc / parj);
        d[2 * SIZE_OF_SAMPLE - 1 - i] = pow(abs(((i)*1.0) / SIZE_OF_SAMPLE - y), (double) parc / parj);

        /*    outPP << y << " " << ((i+1)*1.0)/SIZE_OF_SAMPLE << "    " << ((i)*1.0)/SIZE_OF_SAMPLE;
            outPP << endl; */

    }


    int i, j, l, r;
    double x, w;
    int size = 2 * SIZE_OF_SAMPLE;
    double* array = d;

    Stack z = Stack(size / 2 + 1); // inicializace zasobniku
    z.Insert(0, size - 1);
    do {
        z.Remove(&l, &r);
        do {
            i = l;
            j = r;
            x = array[(l + r) / 2];
            do {
                while (array[i] < x) i++;
                while (array[j] > x) j--;
                if (i <= j) {
                    w = array[i];
                    array[i] = array[j];
                    array[j] = w;
                    i++;
                    j--;
                }
            } while (i < j);
            if (i < r) z.Insert(i, r);
            r = j;
        } while (l <= r);
    }    while (!z.Empty());

    array = NULL;



    //  Vypisuje součty poli do souboru
    /*    QFile fileP(adresar+"P.txt");
        fileP.open(QIODevice::Append);
        QTextStream outP(&fileP);
        double soucetP = 0;
          for(int jjj = 0; jjj< size; jjj++)
              //outP << d[jjj] << "  ";
                soucetP=soucetP+d[jjj];
          outP << soucetP << endl;

          fileP.close();
     */



    for (int i = 0; i <= h; i++) {
        int U = floor(vyber[i]);
        e = e + pow(2 * SIZE_OF_SAMPLE - U, exp) * d[(int) U];
    }

    e = e / pow((double) m, exp + 1);
    e = e + d[2 * SIZE_OF_SAMPLE - 1] / (k * m);

    return e;
}
