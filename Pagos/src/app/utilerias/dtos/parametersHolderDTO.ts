import { ParametersMap } from './parametersMapDTO';
import { StringsMap } from './stringsMapDTO';

export class ParametersHolder {
    parametersMap: Map<String, Object> | undefined;
    stringsMap: Map<String, String> | undefined;
    parametersMapList: Array<ParametersMap> | undefined;
    stringsMapList: Array<StringsMap> | undefined;
}
export class ParametersHolder_ {
    parametersMap: ParamEntid | undefined;
    stringsMap: Map<String, String> | undefined;
    parametersMapList: Array<ParametersMap> | undefined;
    stringsMapList: Array<StringsMap> | undefined;
}

export class ParamEntid {
    financialEntitiesList: Array<number> | undefined;
}
