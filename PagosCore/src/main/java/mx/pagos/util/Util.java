package mx.pagos.util;

import mx.pagos.admc.enums.security.UserStatusEnum;


public class Util {
	
	

	public static UserStatusEnum getStatusUsuarioEnum(Integer id) {
		for(UserStatusEnum tmp:UserStatusEnum.values()){
			if(tmp.ordinal()==id)
				return tmp;
		}
		return UserStatusEnum.ACTIVE;
	}
	
	
}
