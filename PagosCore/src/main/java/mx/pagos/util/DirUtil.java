package mx.pagos.util;

import java.io.File;

public class DirUtil {
	
	private static Integer nv0=0;
	private final static Integer nv1=5;
	private final static Integer nv2=25;
	private static final String nivel1="K";
	private static final String nivel2="M";
	private static final String separador="-";
	private static final String separadorRuta=File.separator;
	
	public static void main(String[] args) {
		System.out.println(obtenRutaSolicitud(5));
		System.out.println(DirUtil.obtenRutaSolicitud("5"));
	}
	
	public static String obtenRutaSolicitud(Integer idSolicitud){
		return path(getFullString(idSolicitud, "0", 10));
	}
	
	public static String obtenRutaSolicitud(String idSolicitud){
		return path(getFullString(idSolicitud, "0", 10));
	}
	
	  public static String path(String folio){
		  String n2=folio.substring(0, 4);
		  String n3=folio.substring(4, 7);
		  Integer valN2=Integer.valueOf(n2);
		  Integer valN3=Integer.valueOf(n3);
		  Long valN4=Long.valueOf(folio);
		  String capeta2=nombreCarpeta(nv0,valN2, nv2, nivel2,null,valN4);
		  String capeta3=nombreCarpeta(nv0,valN3, nv1, nivel1,String.valueOf(valN2),valN4);
		  return separadorRuta+capeta2+separadorRuta+capeta3;
	  }
	  
	public static String nombreCarpeta(final int init,int val,int nivVal,String end,String prefix,long ending){
			Integer pre=0;

				if(init==nv0){
					if(val<nivVal){
						if(prefix!=null)
							pre=Integer.valueOf(prefix)*1000;
						
						if(pre+nv0==0){
							return (pre+nv0)+separador+(pre+nivVal)+end;
						}
						else{
							if(ending==0)
								return (pre+nv0-nv1)+end+separador+(pre+nivVal-nv1)+end;
							else if((ending%1000000)==0){
								Long value=(long) (pre+nivVal-nv1);
								if(((value)%1000)==0)
									return (pre+nv0-nv1)+end+separador+(value/1000)+nivel2;
									
								return (pre+nv0-nv1)+end+separador+(pre+nivVal-nv1)+end;
							}
							Long value=(long) (pre+nv0);
							if(((value)%1000)==0)
								return (value/1000)+nivel2+separador+(pre+nivVal)+end;
							return value+end+separador+(pre+nivVal)+end;
						}
					}
				}
				
				if((val-nivVal)<init){
						if(prefix!=null)
							pre=Integer.valueOf(prefix)*1000;
						if(((pre+init)*1000)==(ending)){
							if(init==nv1 && (pre+init-nv1)==nv0 )
								return (pre+init-nv1)+separador+(pre+(init+nivVal)-nv1)+end;
							
							Long value=(long) (pre+init-nv1);
							if(((value)%1000)==0)
								return (value/1000)+nivel2+separador+(pre+(init+nivVal)-nv1)+end;
							
							return value+end+separador+(pre+(init+nivVal)-nv1)+end;
						}else{
							if((ending%1000000)==0)
								return (pre+init-nivVal)+separador+(pre+nivVal+init-nivVal)+end;
							
							
							Long value=(long) (pre+(init+nivVal));
							if(((value)%1000)==0)
								return (pre+init)+end+separador+(value/1000)+nivel2;
							
							return (pre+init)+end+separador+value+end;
						}
				}else{
					if(ending==0){
						if(prefix!=null)
							pre=Integer.valueOf(prefix)*1000;
						return (pre+init)+end+separador+(pre+(init+nivVal))+end;
					}else
						return nombreCarpeta((init+nivVal),val,nivVal,end,prefix,ending);
					
				}
			
		}
	
	
	public static String getFullString(String cadena, String prefijo, int longitudMaxima){
        StringBuffer res = new StringBuffer();        ; 
        for (int i = 0; i < (longitudMaxima-cadena.trim().length()); i++) {        ;     
            res.append(prefijo);
        }
        res.append(cadena);
        return res.toString();
	} 
	
	public static String getFullString(Integer cadInt, String prefijo, int longitudMaxima){
		String cadena=cadInt.toString();
		return getFullString(cadena, prefijo, longitudMaxima);
	} 

}
