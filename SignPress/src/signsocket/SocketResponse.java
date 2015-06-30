package signsocket;

public abstract class SocketResponse<T> {
	/**
	 * 杩斿洖鏁版嵁璇锋眰鐨勭粨鏋滃�
	 * @param response
	 *  
	 */
	  public abstract void onResponse(T response);
}
