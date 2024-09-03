package id.ac.polman.astra.kel10.catetinaja;

import android.os.Build;
import android.util.Patterns;

import androidx.annotation.RequiresApi;

import java.text.NumberFormat;
import java.util.Locale;

public class Validation {
    public static boolean matchEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String formatRupiah(Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }
}