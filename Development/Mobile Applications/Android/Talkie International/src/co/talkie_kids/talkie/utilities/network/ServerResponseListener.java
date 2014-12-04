package co.talkie_kids.talkie.utilities.network;

public interface ServerResponseListener {

	/**
	 * Post action.
	 * 
	 * @param isSuccessful
	 *            the is http request successful
	 * @param result
	 *            the result
	 */
	void postAction(boolean isSuccessful, Object result);

	/**
	 * Pre execute action.
	 */
	void preExecuteAction();
}