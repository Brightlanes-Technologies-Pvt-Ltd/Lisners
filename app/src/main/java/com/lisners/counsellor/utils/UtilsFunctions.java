package com.lisners.counsellor.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lisners.counsellor.ApiModal.SpacializationMedel;
import com.lisners.counsellor.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class UtilsFunctions {

    private static final String[] suffix = new String[]{"","k", "m", "b", "t"};
    private static final int MAX_LENGTH = 4;

    public static String formatNumber(String number) {

        try {
            if(number!=null) {
                String r = new DecimalFormat("##0E0").format(number);
                r = r.replaceAll("E[0-9]", suffix[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]);
                while (r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]")) {
                    r = r.substring(0, r.length() - 2) + r.substring(r.length() - 1);
                }
                return r;
            }
            else
                return number+"" ;
        }catch (Exception e){
            return number+"" ;
        }

    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +"[a-zA-Z0-9_+&*-]+)*@" +"(?:[a-zA-Z0-9-]+\\.)+[a-z" +"A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static boolean isValidPass(String email) {
        String emailRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static String errorShow(JSONArray array) {
        try {
            if (array != null) {
                for (int i = 0; i < array.length(); i++)
                    return array.getString(i) + "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){}

        return "";
    }

    public static void SetLOGO(Context context, String URL, ImageView imageView) {
        try {
            Glide
                    .with(context)
                    .load(URL)
                    .placeholder(R.drawable.button_light_primary)
                    .into(imageView);
        } catch (RuntimeException e) {
            e.getStackTrace();
        }
    }


    public static void SetLOGOWithRoundedCorners(Context context, String URL, ImageView imageView,int radius) {
        try {

            Picasso.get()
                    .load(URL)
                    .resize(50,50)
                    .transform(new RoundedCornersTransformation(radius, 0,
                            RoundedCornersTransformation.CornerType.ALL))
                    .placeholder(R.drawable.button_light_primary)
                    .into(imageView);
        } catch (RuntimeException e) {
            e.getStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String fullDateFormat(String dateSt) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy | HH:mm aa");
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date date = format.parse(dateSt);
           return formatter.format(date);
        }catch (Exception e){
            return dateSt ;
        }
    }

    public static String dateFormat(String dateSt) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = format.parse(dateSt);
            return formatter.format(date);
        }catch (Exception e){
            return dateSt ;
        }
    }


    public static String convertTime(String time)
    {
        String format;
        String[] arr = time.split(":");

        int hh = Integer.parseInt(arr[0]);

        if (hh > 12) {
            hh = hh - 12;
            format = "PM";
        }
        else if (hh == 00) {
            hh = 12;
            format = "AM";
        }
        else if (hh == 12) {
            hh = 12;
            format = "PM";
        }
        else {
            format = "AM";
        }

        String hour = String.format("%02d", hh);

        String minute = arr[1];
        String second = arr[2];


        return hour + ":" + minute +" "+ format;
    }


    public static String timeFormat(String dateSt) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm aa");
            DateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            Date date = format.parse(dateSt);
            return formatter.format(date);
        }catch (Exception e){
            return dateSt ;
        }
    }

    public static String getSpecializeString(ArrayList<SpacializationMedel> dateSt) {
        try {
            String st = "";
            int size = dateSt.size();
            if (size > 2) {
                st = dateSt.get(0).getTitle() + ", " + dateSt.get(1).getTitle() + " +" + (dateSt.size() - 2);
            } else {
                for (SpacializationMedel medel : dateSt) {
                    if (st.isEmpty())
                        st = medel.getTitle();
                    else
                        st = st + ", " + medel.getTitle();

                }
            }
            return st+" ";
        } catch (Exception e) {
            return "";
        }
    }

    public static String getFistLastChar( String name) {
        String s ="";
        try {
            String[] strings = name.split(" ");
            for(String st :strings){
                if(s.isEmpty())
                  s= st.charAt(0)+"";
                else {
                    s = s + st.charAt(0);
                    break;
                }
            }

        } catch (Exception e) {
            return name ;
        }finally {
            if(s.isEmpty())
                return name.charAt(0)+"";
            else
                return s.toUpperCase();
        }

    }

    public static long getMillisecondsRAnge(String start,String end) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:aa");
            Date stdate = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).parse(start);
            Date stend = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).parse(end);
            return stend.getTime()-stdate.getTime();
        }catch (Exception e){

            return  10*60*1000 ;
        }
    }


    public static String splitCamelCase(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        StringBuilder converted = new StringBuilder();
        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }

        return converted.toString();
    }


    public static   String  calculateTime(long seconds) {
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);
        if(day<=0)
            return "  H:" + hours + " M:" + minute + " S:" + second +"  ";
        else return "  D:" + day + " H:" + hours + " M:" + minute + " S:" + second +"  ";
    }

}
