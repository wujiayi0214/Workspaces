package signsocket;

public enum ServerResponse {
	LOGIN_SUCCESS,
	LOGIN_FAILED,
	
    /// <summary>
    /// ǩ���˲�ѯ��ǩ��״̬����
    /// </summary>
    QUERY_UNSIGN_CONTRACT_SUCCESS,
    QUERY_UNSIGN_CONTRACT_FAILED,

    QUERY_SIGNED_CONTRACT_SUCCESS,
    QUERY_SIGNED_CONTRACT_FAILED,

    INSERT_SIGN_DETAIL_SUCCESS,
    INSERT_SIGN_DETAIL_FAILED,

    QUERY_SIGN_DETAIL_SUCCESS,
    QUERY_SIGN_DETAIL_FAILED,

    QUERY_SIGN_DETAIL_CON_SUCCESS,
    QUERY_SIGN_DETAIL_CON_FAILED,
    
    GET_HDJCONTRACT_SUCCESS,
    GET_HDJCONTRACT_FAILED,
}
