package com.kickstarter.services.apiresponses;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import com.kickstarter.libs.qualifiers.AutoGson;
import com.kickstarter.libs.utils.ListUtils;
import com.kickstarter.services.ApiError;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import auto.parcel.AutoParcel;

@AutoGson
@AutoParcel
public abstract class ErrorEnvelope implements Parcelable {
  @Nullable public abstract List<String> errorMessages();
  public abstract int httpCode();
  @Nullable public abstract String ksrCode();
  @Nullable public abstract FacebookUser facebookUser();

  @AutoGson
  @AutoParcel
  public static abstract class FacebookUser implements Parcelable {
    public abstract long id();
    public abstract String name();
    public abstract String email();

    @AutoParcel.Builder
    public static abstract class Builder {
      public abstract Builder id(long __);
      public abstract Builder name(String __);
      public abstract Builder email(String __);
      public abstract FacebookUser build();
    }

    public static Builder builder() {
      return new AutoParcel_ErrorEnvelope_FacebookUser.Builder();
    }

    public abstract Builder toBuilder();
  }

  public static final String CONFIRM_FACEBOOK_SIGNUP = "confirm_facebook_signup";
  public static final String INVALID_XAUTH_LOGIN = "invalid_xauth_login";
  public static final String TFA_FAILED = "tfa_failed";
  public static final String TFA_REQUIRED = "tfa_required";
  public static final String MISSING_FACEBOOK_EMAIL = "missing_facebook_email";
  public static final String FACEBOOK_INVALID_ACCESS_TOKEN = "facebook_invalid_access_token";

  @StringDef({INVALID_XAUTH_LOGIN, TFA_FAILED, TFA_REQUIRED})
  @Retention(RetentionPolicy.SOURCE)
  public @interface ErrorCode {}

  /*
    Tries to extract an api error envelope from an exception, and if it
     can't returns `null`.
   */
  @Nullable public static ErrorEnvelope fromThrowable(@NonNull final Throwable e) {
    if (e instanceof ApiError) {
      final ApiError error = (ApiError) e;
      final ErrorEnvelope envelope = error.errorEnvelope();
      return envelope;
    }

    return null;
  }

  public boolean isConfirmFacebookSignupError() {
    return ksrCode().equals(CONFIRM_FACEBOOK_SIGNUP);
  }

  public boolean isInvalidLoginError() {
    return ksrCode().equals(INVALID_XAUTH_LOGIN);
  }

  public boolean isTfaRequiredError() {
    return ksrCode().equals(TFA_REQUIRED);
  }

  public boolean isTfaFailedError() {
    return ksrCode().equals(TFA_FAILED);
  }

  public boolean isMissingFacebookEmailError() {
    return ksrCode().equals(MISSING_FACEBOOK_EMAIL);
  }

  public boolean isFacebookInvalidAccessTokenError() {
    return ksrCode().equals(FACEBOOK_INVALID_ACCESS_TOKEN);
  }

  /*
    When logging in the only two possible errors are INVALID_XAUTH_LOGIN
    and TFA_REQUIRED, so we consider anything else an unknown error.
   */
  public boolean isGenericLoginError() {
    return
      !ksrCode().equals(INVALID_XAUTH_LOGIN) &&
        !ksrCode().equals(TFA_REQUIRED);
  }

  /**
   * Returns the first error message available, or `null` if there are none.
   */
  public @Nullable String errorMessage() {
    return ListUtils.first(errorMessages());
  }
}
