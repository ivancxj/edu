package com.edu.lib.api;

import ch.boye.httpclientandroidlib.Header;
import ch.boye.httpclientandroidlib.HttpStatus;
import ch.boye.httpclientandroidlib.client.HttpResponseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.os.Message;
import android.text.TextUtils;

import com.edu.lib.util.LogUtils;
import com.edu.lib.util.UIUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Used to intercept and handle the responses from requests made using
 * {@link AsyncHttpClient}, with automatic parsing into a {@link JSONObject} or
 * {@link JSONArray}.
 * <p>
 * This class is designed to be passed to get, post, put and delete requests
 * with the {@link #onSuccess(JSONObject)} or {@link #onSuccess(JSONArray)}
 * methods anonymously overridden.
 * <p>
 * Additionally, you can override the other event methods from the parent class.
 */
public class JsonHandler extends AsyncHttpResponseHandler {
	protected static final int SUCCESS_JSON_MESSAGE = 100;

	private Activity activity;

	public JsonHandler(Activity activity) {
		this.activity = activity;
	}

	public Activity getHandlerActivity() {
		return this.activity;
	}

	//
	// Callbacks to be overridden, typically anonymously
	//

	/**
	 * Fired when a request returns successfully and contains a json object at
	 * the base of the response string. Override to handle in your own code.
	 * 
	 * @param response
	 *            the parsed json object found in the server response (if any)
	 */
	public void onSuccess(JSONObject response) {
	}

	/**
	 * Fired when a request returns successfully and contains a json array at
	 * the base of the response string. Override to handle in your own code.
	 * 
	 * @param response
	 *            the parsed json array found in the server response (if any)
	 */
	public void onSuccess(JSONArray response) {
	}

	/**
	 * Fired when a request returns successfully and contains a json object at
	 * the base of the response string. Override to handle in your own code.
	 * 
	 * @param statusCode
	 *            the status code of the response
	 * @param response
	 *            the parsed json object found in the server response (if any)
	 */
//	public void onSuccess(int statusCode, JSONObject response) {
//		onSuccess(response);
//	}

	/**
	 * Fired when a request returns successfully and contains a json array at
	 * the base of the response string. Override to handle in your own code.
	 * 
	 * @param statusCode
	 *            the status code of the response
	 * @param response
	 *            the parsed json array found in the server response (if any)
	 */
//	public void onSuccess(int statusCode, JSONArray response) {
//		onSuccess(response);
//	}

	@Override
	public void onFailure(Throwable error, String content) {
		if (TextUtils.isEmpty(content)) {
			if (!TextUtils.isEmpty(error.getMessage()))
				showFailToast(error.getMessage());
		} else {
			showFailToast(content);
//			if (error instanceof HttpResponseException) {
//				HttpResponseException ex = (HttpResponseException) error;
//				if (ex.getStatusCode() >= 500) {
//					if (this.activity != null) {
//						showFailToast("服务器内部错误");
//					} else {
//						showFailToast(content);
//					}
//				}
//				if (ex.getStatusCode() == 404) {
//					ex.printStackTrace();
//					showFailToast("访问的页面不存在");
//				}
//
//			}
		}
	}

	public void onFailure(Throwable e, JSONObject errorResponse) {
		if(errorResponse != null)
			LogUtils.E(LogUtils.SERVER_RETURN, e.getMessage());
		
		if (e instanceof HttpResponseException) {
			HttpResponseException ex = (HttpResponseException) e;
			LogUtils.E(LogUtils.SERVER_RETURN, "ex status code = "+ex.getStatusCode());
		}
	}

	public void onFailure(Throwable e, JSONArray errorResponse) {
		if(errorResponse != null)
			LogUtils.E(LogUtils.SERVER_RETURN, e.getMessage()+" "+errorResponse.toString());
	}

	protected void showFailToast(String errorMessage) {
		if (this.activity != null) {
			if (!TextUtils.isEmpty(errorMessage)) {
				LogUtils.E(LogUtils.SERVER_RETURN, errorMessage);
				UIUtils.showErrToast(this.activity, errorMessage);
			}
		}
	}

	//
	// Pre-processing of messages (executes in background threadpool thread)
	//

    @Override
    protected void sendSuccessMessage(int statusCode, Header[] headers, String responseBody) {
        if (statusCode != HttpStatus.SC_NO_CONTENT){        
            try {
                Object jsonResponse = parseResponse(responseBody);
	        sendMessage(obtainMessage(SUCCESS_JSON_MESSAGE, new Object[]{statusCode, headers, jsonResponse}));
    	    } catch(JSONException e) {
    	        sendFailureMessage(e, responseBody);
    	    }
        } else {
            sendMessage(obtainMessage(SUCCESS_JSON_MESSAGE, new Object[]{statusCode, new JSONObject()}));
    	}
    }


	//
	// Pre-processing of messages (in original calling thread, typically the UI
	// thread)
	//

	@Override
	protected void handleMessage(Message msg) {
		switch (msg.what) {
		case SUCCESS_JSON_MESSAGE:
            Object[] response = (Object[]) msg.obj;
            handleSuccessJsonMessage(((Integer) response[0]).intValue(),(Header[]) response[1] ,response[2]);
//			Object[] response = (Object[]) msg.obj;
//			handleSuccessJsonMessage(((Integer) response[0]).intValue(),
//					response[1]);
			break;
		default:
			super.handleMessage(msg);
		}
	}
	
    protected void handleSuccessJsonMessage(int statusCode,Header[] headers, Object jsonResponse) {
		if (jsonResponse instanceof JSONObject) {
			// ==========
			JSONObject response = (JSONObject) jsonResponse;
			if(response.has("ErrorCode")){
				if(response.optInt("ErrorCode") == 0){
					onSuccess(response);
				}else{
					showFailToast(response.optString("ErrorInfo"));
				}
			}else{
				showFailToast("返回格式有误（无ErrorCode）");
			}
			// ==========
		} else if (jsonResponse instanceof JSONArray) {
			onSuccess((JSONArray) jsonResponse);
		} else {
			onFailure(new JSONException("Unexpected type "
					+ jsonResponse.getClass().getName()), (JSONObject) null);
		}
//        if(jsonResponse instanceof JSONObject) {
//            onSuccess(statusCode, headers, (JSONObject)jsonResponse);
//        } else if(jsonResponse instanceof JSONArray) {
//            onSuccess(statusCode, headers, (JSONArray)jsonResponse);
//        } else {
//            onFailure(new JSONException("Unexpected type " + jsonResponse.getClass().getName()), (JSONObject)null);
//        }
    }

	protected Object parseResponse(String responseBody) throws JSONException {
		Object result = null;
		// trim the string to prevent start with blank, and test if the string
		// is valid JSON, because the parser don't do this :(. If Json is not
		// valid this will return null
		responseBody = responseBody.trim();
		if (responseBody.startsWith("{") || responseBody.startsWith("[")) {
			result = new JSONTokener(responseBody).nextValue();
		}
		if (result == null) {
			result = responseBody;
		}
		return result;
	}

	@Override
	protected void handleFailureMessage(Throwable e, String responseBody) {
		try {
			if (responseBody != null) {
				Object jsonResponse = parseResponse(responseBody);
				if (jsonResponse instanceof JSONObject) {
					onFailure(e, (JSONObject) jsonResponse);
				} else if (jsonResponse instanceof JSONArray) {
					onFailure(e, (JSONArray) jsonResponse);
				} else {
					onFailure(e, responseBody);
				}
			} else {
				onFailure(e, "");
			}
		} catch (JSONException ex) {
			onFailure(e, responseBody);
		}
	}
}
