package com.covetus.audit;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import ABS_CUSTOM_VIEW.EditTextRegular;
import ABS_HELPER.AppController;
import ABS_HELPER.CommonUtils;
import ABS_HELPER.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ly.img.android.ui.activities.CameraPreviewActivity;
import ly.img.android.ui.activities.CameraPreviewIntent;
import ly.img.android.ui.activities.PhotoEditorIntent;
import ly.img.android.ui.utilities.PermissionRequest;

import static ABS_HELPER.CommonUtils.FOLDER;
import static ABS_HELPER.CommonUtils.hidePDialog;
import static ABS_HELPER.CommonUtils.mCheckSignalStrength;
import static ABS_HELPER.CommonUtils.mShowAlert;
import static ABS_HELPER.CommonUtils.mStrBaseUrlImage;

public class ActivityUpdateProfile extends Activity implements PermissionRequest.Response {


    public static int CAMERA_PREVIEW_RESULT = 1;
    String mStrFirstName, mStrLastName, mStrEmailAddress, mStrContactNumber, imageString = "";

    @BindView(R.id.mEditFirstName)
    EditTextRegular mEditFirstName;
    @BindView(R.id.mEditLastName)
    EditTextRegular mEditLastName;
    @BindView(R.id.mEditEmailAddress)
    EditTextRegular mEditEmailAddress;
    @BindView(R.id.mEditContactNumber)
    EditTextRegular mEditContactNumber;
    @BindView(R.id.mLayoutUpdate)
    RelativeLayout mLayoutUpdate;

    @BindView(R.id.mImageBack)
    ImageView mImageBack;
    @BindView(R.id.mImgUserProfile)
    ImageView mImgUserProfile;
    @BindView(R.id.mImgAddImage)
    ImageView mImgAddImage;
    private static int SPLASH_TIME_OUT = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        ButterKnife.bind(this);


        mEditFirstName.setText(PreferenceManager.getFormiiFirstName(ActivityUpdateProfile.this));
        mEditLastName.setText(PreferenceManager.getFormiiLastName(ActivityUpdateProfile.this));
        mEditEmailAddress.setText(PreferenceManager.getFormiiEmail(ActivityUpdateProfile.this));
        mEditContactNumber.setText(PreferenceManager.getFormiiContact(ActivityUpdateProfile.this));
        System.out.println("<><>" + mStrBaseUrlImage + PreferenceManager.getFormiiProfileimg(ActivityUpdateProfile.this));
        Glide.with(ActivityUpdateProfile.this).load(mStrBaseUrlImage + PreferenceManager.getFormiiProfileimg(ActivityUpdateProfile.this)).asBitmap().centerCrop().placeholder(R.drawable.placeholder_user_profile).into(new BitmapImageViewTarget(mImgUserProfile) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(ActivityUpdateProfile.this.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                mImgUserProfile.setImageDrawable(circularBitmapDrawable);
            }
        });


        mEditContactNumber.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    CommonUtils.closeKeyBoard(ActivityUpdateProfile.this);
                    CommonUtils.OnClick(ActivityUpdateProfile.this, mLayoutUpdate);
                    mStrFirstName = mEditFirstName.getText().toString();
                    mStrLastName = mEditLastName.getText().toString();
                    mStrEmailAddress = mEditEmailAddress.getText().toString();
                    mStrContactNumber = mEditContactNumber.getText().toString();

        /* validation for updating profile*/
                    if (mStrFirstName.length() <= 0) {
                        CommonUtils.mShowAlert(getString(R.string.mTextFile_error_enter_first_name), ActivityUpdateProfile.this);
                        return false;
                    } else if (mStrLastName.length() <= 0) {
                        CommonUtils.mShowAlert(getString(R.string.mTextFile_error_enter_last_name), ActivityUpdateProfile.this);
                        return false;
                    } else if (mStrContactNumber.length() <= 0) {
                        CommonUtils.mShowAlert(getString(R.string.mTextFile_error_enter_contact), ActivityUpdateProfile.this);
                        return false;
                    } else if (mStrContactNumber.length() < 10 || mStrContactNumber.length() > 10) {
                        CommonUtils.mShowAlert(getString(R.string.mTextFile_error_valid_no), ActivityUpdateProfile.this);
                        return false;
                    }

                    if (mCheckSignalStrength(ActivityUpdateProfile.this)==2) {
                        CommonUtils.showPDialog(ActivityUpdateProfile.this);
                        mToDoUpdate();
                    } else {
                        mShowAlert(getString(R.string.mTextFile_alert_no_internet), ActivityUpdateProfile.this);
                    }
                    return true;
                }
                return false;
            }
        });


    }

    /* click for going back */
    @OnClick(R.id.mImageBack)
    public void mGoBack() {
        finish();
    }

    /* click to add image*/
    @OnClick(R.id.mImgAddImage)
    public void mImageAddImg() {
        new CameraPreviewIntent(this)
                .setExportDir(CameraPreviewIntent.Directory.DCIM, FOLDER)
                .setExportPrefix(getString(R.string.mTextFile_imagename))
                .setEditorIntent(
                        new PhotoEditorIntent(this)
                                .setExportDir(PhotoEditorIntent.Directory.DCIM, FOLDER)
                                .setExportPrefix(getString(R.string.mTextFile_filename))
                                .destroySourceAfterSave(true)
                )
                .startActivityForResult(CAMERA_PREVIEW_RESULT);
    }

    /* click for update profile */
    @OnClick(R.id.mLayoutUpdate)
    public void mLayoutUpdate() {
        CommonUtils.OnClick(ActivityUpdateProfile.this, mLayoutUpdate);
        mStrFirstName = mEditFirstName.getText().toString();
        mStrLastName = mEditLastName.getText().toString();
        mStrEmailAddress = mEditEmailAddress.getText().toString();
        mStrContactNumber = mEditContactNumber.getText().toString();

        /* validation for updating profile*/
        if (mStrFirstName.length() <= 0) {
            CommonUtils.mShowAlert(getString(R.string.mTextFile_error_enter_first_name), ActivityUpdateProfile.this);
            return;
        } else if (mStrLastName.length() <= 0) {
            CommonUtils.mShowAlert(getString(R.string.mTextFile_error_enter_last_name), ActivityUpdateProfile.this);
            return;
        } else if (mStrContactNumber.length() <= 0) {
            CommonUtils.mShowAlert(getString(R.string.mTextFile_error_enter_contact), ActivityUpdateProfile.this);
            return;
        } else if (mStrContactNumber.length() < 10 || mStrContactNumber.length() > 10) {
            CommonUtils.mShowAlert(getString(R.string.mTextFile_error_valid_no), ActivityUpdateProfile.this);
            return;
        }

        if (mCheckSignalStrength(ActivityUpdateProfile.this)==2) {
            CommonUtils.showPDialog(ActivityUpdateProfile.this);
            mToDoUpdate();
        } else {
            mShowAlert(getString(R.string.mTextFile_alert_no_internet), ActivityUpdateProfile.this);
        }
    }

    /* getting result for image from gallery or camera */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CAMERA_PREVIEW_RESULT) {
            String path = data.getStringExtra(CameraPreviewActivity.RESULT_IMAGE_PATH);
            Toast.makeText(this, "Image saved at: " + path, Toast.LENGTH_LONG).show();
            File mMediaFolder = new File(path);
            Glide.with(ActivityUpdateProfile.this).load(mMediaFolder).asBitmap().centerCrop().placeholder(R.drawable.placeholder_user_profile).into(new BitmapImageViewTarget(mImgUserProfile) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(ActivityUpdateProfile.this.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    mImgUserProfile.setImageDrawable(circularBitmapDrawable);
                }
            });

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] imageBytes = baos.toByteArray();
            imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            System.out.print("<><><>" + imageString);
        }
        // finish();
    }

    /* api call to update profile*/
    void mToDoUpdate() {
        StringRequest strRequest = new StringRequest(Request.Method.POST, CommonUtils.mStrBaseUrl + "updateUser",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        hidePDialog();
                        try {
                            System.out.println("<><><>" + str);
                            JSONObject response = new JSONObject(str);
                            String mStrStatus = response.getString("status");
                            if (mStrStatus.equals("1")) {
                                JSONObject jsonObject = response.getJSONObject("response");
                                String mStrId = jsonObject.getString("id");
                                String mStrFirstName = jsonObject.getString("firstname");
                                String mStrLastName = jsonObject.getString("lastname");
                                String mStrEmail = jsonObject.getString("email");
                                String mStrPhoto = jsonObject.getString("photo");
                                String mStrPhone = jsonObject.getString("phone");
                                String mStrAddressEN = jsonObject.getString("address");
                                String mStrAuthToken = jsonObject.getString("auth_token");
                                PreferenceManager.setFormiiId(ActivityUpdateProfile.this, mStrId);
                                PreferenceManager.setFormiiFirstName(ActivityUpdateProfile.this, mStrFirstName);
                                PreferenceManager.setFormiiLastName(ActivityUpdateProfile.this, mStrLastName);
                                PreferenceManager.setFormiiEmail(ActivityUpdateProfile.this, mStrEmail);
                                PreferenceManager.setFormiiProfileimg(ActivityUpdateProfile.this, mStrPhoto);
                                PreferenceManager.setFormiiContact(ActivityUpdateProfile.this, mStrPhone);
                                PreferenceManager.setFormiiAddress(ActivityUpdateProfile.this, mStrAddressEN);
                                PreferenceManager.setFormiiFullName(ActivityUpdateProfile.this, mStrFirstName + " " + mStrLastName);
                                PreferenceManager.setFormiiIsLogin(ActivityUpdateProfile.this, "1");
                                PreferenceManager.setFormiiAuthToken(ActivityUpdateProfile.this, mStrAuthToken);
                                CommonUtils.mShowAlert(getString(R.string.mText_success), ActivityUpdateProfile.this);
                                new Handler().postDelayed(new Runnable() {

                                    @Override
                                    public void run() {


                                        finish();


                                    }
                                }, SPLASH_TIME_OUT);

                            } else if (mStrStatus.equals("2")) {
                                CommonUtils.showSessionExp(ActivityUpdateProfile.this);
                            } else {
                                CommonUtils.mShowAlert(response.getString("message"), ActivityUpdateProfile.this);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        Toast.makeText(ActivityUpdateProfile.this, getString(R.string.mTextFile_error_something_went_wrong) + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("id", PreferenceManager.getFormiiId(ActivityUpdateProfile.this));
                params.put("firstname", mStrFirstName);
                params.put("lastname", mStrLastName);
                params.put("photo", imageString);
                params.put("phone", mStrContactNumber);
                params.put("platform", "android");
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                PreferenceManager.setFormiiDeviceId(ActivityUpdateProfile.this, refreshedToken);
                params.put("devicetoken", PreferenceManager.getFormiiDeviceId(ActivityUpdateProfile.this));
                params.put("auth_token", PreferenceManager.getFormiiAuthToken(ActivityUpdateProfile.this));


                System.out.println("<><><>param" + params);
                return params;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strRequest);
    }


    /* permmission request*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionRequest.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /* permmission grant*/
    @Override
    public void permissionGranted() {

    }

    /* permmission denied*/
    @Override
    public void permissionDenied() {
        finish();
        System.exit(0);
    }
}
