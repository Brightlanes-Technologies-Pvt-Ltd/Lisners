package com.lisners.counsellor.Activity.Home.AvailabilityStack;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.google.gson.Gson;
import com.lisners.counsellor.Activity.Functions.MySpannable;
import com.lisners.counsellor.ApiModal.BookedAppointment;
import com.lisners.counsellor.ApiModal.SpacializationMedel;
import com.lisners.counsellor.ApiModal.User;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.DProgressbar;
import com.lisners.counsellor.utils.GetApiHandler;
import com.lisners.counsellor.utils.PostApiHandler;
import com.lisners.counsellor.utils.PutApiHandler;
import com.lisners.counsellor.utils.URLs;
import com.lisners.counsellor.utils.UtilsFunctions;
import com.lisners.counsellor.zWork.utils.DateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddPrescriptionFragment extends Fragment implements View.OnClickListener {
    TextView tvHeader;
    ImageButton btn_header_left;
    BookedAppointment bookApp;
    TextView tv_name, tv_parsley_notes, tv_note_title, tv_date_time, tv_spacilization, tv_sec_time;
    EditText edit_prescription;
    Button btn_sign_in;
    DProgressbar dProgressbar;
    ImageView p_image;
    TextView tv_short_name;
    ImageButton ivPhoneIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_prescription, container, false);
        init(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void init(View view) {
        p_image = view.findViewById(R.id.p_image);
        tv_short_name = view.findViewById(R.id.tv_short_name);
        tvHeader = view.findViewById(R.id.tvHeader);
        btn_header_left = view.findViewById(R.id.btn_header_left);
        btn_header_left.setImageResource(R.drawable.ic_svg_arrow_right);
        btn_header_left.setOnClickListener(this);
        tvHeader.setText("Add Prescription");
        dProgressbar = new DProgressbar(getContext());
        tv_name = view.findViewById(R.id.tv_name);
        tv_parsley_notes = view.findViewById(R.id.tv_parsley_notes);
        tv_date_time = view.findViewById(R.id.tv_date_time);
        tv_spacilization = view.findViewById(R.id.tv_spacilization);
        edit_prescription = view.findViewById(R.id.edit_prescription);
        tv_note_title = view.findViewById(R.id.tv_note_title);
        tv_sec_time = view.findViewById(R.id.tv_sec_time);
        btn_sign_in = view.findViewById(R.id.btn_sign_in);

        ivPhoneIcon = view.findViewById(R.id.iv_phone_icon);
        btn_sign_in.setOnClickListener(this);

        getBookAppData();

    }

    private void getBookAppData() {
        btn_sign_in.setVisibility(View.GONE);
        Bundle bundle = this.getArguments();
        String bookID = bundle.getString("BOOK_ID", "NO");
        String hide = bundle.getString("HIDE", "NO");

        if (!bookID.equals("NO")) {
            GetApiHandler apiHandler = new GetApiHandler(getContext(), URLs.SET_BOOK_APPOINTMENT + "/" + bookID, new GetApiHandler.OnClickListener() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    if (jsonObject.has("status") && jsonObject.has("data")) {
                        bookApp = new Gson().fromJson(jsonObject.getString("data"), BookedAppointment.class);
                        ArrayList<SpacializationMedel> spacializationMedels = bookApp.getSpecialization();
                       /* if (spacializationMedels != null) {
                            tv_spacilization.setText(UtilsFunctions.getSpecializeString(spacializationMedels));
                        }*/


                        if (bookApp.getCall_type().equalsIgnoreCase("video")) {
                            ivPhoneIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_svg_video_camera));
                        } else {
                            ivPhoneIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_svg_phone));
                        }


                        tv_spacilization.setText(bookApp.getSpecialization_name());

                        User user = bookApp.getUser();
                        String name = UtilsFunctions.splitCamelCase(user.getName());
                        tv_name.setText(name);
                        tv_note_title.setText(name + "'s Notes");
                        tv_parsley_notes.setText(bookApp.getAdd_notes());
                        edit_prescription.setText(bookApp.getPrescriprion());

                        String date = DateUtil.dateFormatter(bookApp.getDate(), "dd-MM-yyyy", "dd MMM yyyy");
                        String timeRange = String.format("%s - %s", DateUtil.dateFormatter(bookApp.getAppointment_detail().getStart_time(), "HH:mm:ss", "hh:mm a"), DateUtil.dateFormatter(bookApp.getAppointment_detail().getEnd_time(), "HH:mm:ss", "hh:mm a"));
                        tv_date_time.setText(String.format("%s | %s", date, timeRange));

                        tv_sec_time.setText(bookApp.getCall_time() + " sec");
                        makeTextViewResizable(tv_parsley_notes, 3, "See More >>", true);


                        if (user.getProfile_image() != null ) {
                            UtilsFunctions.SetLOGO(getActivity(), user.getProfile_image(), p_image);
                            tv_short_name.setVisibility(View.GONE);
                        } else {
                            tv_short_name.setText(UtilsFunctions.getFistLastChar(user.getName()));
                            tv_short_name.setVisibility(View.VISIBLE);
                        }

                        if (hide.equals("HIDE") && bookApp.getPrescriprion() != null) {

                            edit_prescription.setEnabled(false);
                            //tvHeader.setText(" Prescription");
                            tvHeader.setText("Appointment Details");
                        } else
                            btn_sign_in.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onError() {

                }
            });
            apiHandler.execute();
        }
    }

    private void addPrescription() {
        Bundle bundle = this.getArguments();
        String bookID = bundle.getString("BOOK_ID", "NO");
        String st_pre = edit_prescription.getText().toString();
        Map<String, String> params = new HashMap<>();

        if (st_pre.isEmpty())
            Toast.makeText(getContext(), "Enter Prescription", Toast.LENGTH_SHORT).show();
        else {
            dProgressbar.show();
            params.put("prescriprion", st_pre);
            params.put("_method", "put");

            PutApiHandler putApiHandler = new PutApiHandler(getContext(), URLs.SET_PRESCRIPTION + bookID, params, new PutApiHandler.OnClickListener() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    dProgressbar.dismiss();
                    if (jsonObject.has("status"))
                        getFragmentManager().popBackStack();
                    if (jsonObject.has("message"))
                        Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(ANError error) {
                    dProgressbar.dismiss();
                }
            });
            putApiHandler.execute();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_header_left:
                getFragmentManager().popBackStack();
                break;
            case R.id.btn_sign_in:
                addPrescription();
                break;
        }
    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                  if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new MySpannable(false) {
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "<< See Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
//                        tv.setTextColor(Color.parseColor("#FF0000"));
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, "See More >>", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

}