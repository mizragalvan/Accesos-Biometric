package mx.pagos.admc.enums;

import java.util.LinkedHashMap;

/**
 * 
 * @author Mizraim
 * 
 * Enumerador que contiene los diferentes tipos de personalidades legales existentes
 *
 */
public enum PersonalityEnum {
    NATURALPERSON("Persona Física"),
    JURIDICALPERSON("Persona Moral"),
    FOREIGNNATURALPERSON("Persona Física Extranjera"),
    FOREIGNJURIDICALPERSON("Persona Moral Extranjera"),
    GOVERMENTENTITY("Entidad Gubernamental");
    
    private String personalidad;
    
    PersonalityEnum(final String personalidadParameter) {
        this.personalidad = personalidadParameter;
    }
    
    public String getPersonality() {
        return this.personalidad;
    }
    
    private static LinkedHashMap<String, String> personality;
    
    public final static LinkedHashMap<String, String> getPersonalities(){
        if(personality==null){
            personality=new LinkedHashMap<String, String>();
            for(PersonalityEnum aux:PersonalityEnum.values()){
                personality.put(aux.name(), aux.getPersonality());
            }
        }
        return personality;
    }
}
