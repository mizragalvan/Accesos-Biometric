package mx.pagos.admc.service.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.UsersContractBusiness;
import mx.pagos.admc.contracts.structures.UserLawyerEvaluator;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.security.structures.User;
import mx.pagos.security.structures.UserSession;

@Controller
public class UserServices {

    private static final Logger LOG = Logger.getLogger(UserServices.class);

    @Autowired
    private UserSession session;

    @Autowired 
    private UsersContractBusiness userContractBusiness;

//    @Autowired
//    private Ldapable ldapable;

    @RequestMapping (value = UrlConstants.SEARCH_AVAILABLE_LAWYERS, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<UserLawyerEvaluator> findAviableLawyers(final HttpServletRequest request,
            final HttpServletResponse  response) {
        try {
            LOG.info("UserService :: findAviableLawyers");
            final ConsultaList<UserLawyerEvaluator> aviableLawyersList = new ConsultaList<UserLawyerEvaluator>();
            aviableLawyersList.setList(this.userContractBusiness.findAviableLawyers(this.session.getIdFlow()));
            return aviableLawyersList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
            LOG.error(" BusinessException :: UserService :: findAviableLawyers");
            LOG.error(businessException.getMessage());
        }
        return new ConsultaList<UserLawyerEvaluator>();
    }

    @RequestMapping (value = UrlConstants.SEARCH_AVAILABLE_EVALUATORS, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<UserLawyerEvaluator> findAviableEvaluators(final HttpServletRequest request,
            final HttpServletResponse  response) {
        try {
            LOG.info("UserService :: findAviableEvaluators");
            final ConsultaList<UserLawyerEvaluator> aviableEvaluatorsList = new ConsultaList<UserLawyerEvaluator>();
            aviableEvaluatorsList.setList(this.userContractBusiness.findAviableEvaluators(this.session.getIdFlow()));
            return aviableEvaluatorsList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
            LOG.error(" BusinessException :: UserService :: findAviableEvaluators");
            LOG.error(businessException.getMessage());
        }
        return new ConsultaList<UserLawyerEvaluator>();
    }

    @RequestMapping (value = UrlConstants.SEARCH_AVAILABLE_LAWYERS_BY_FLOW, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<UserLawyerEvaluator> findAviableLawyersByIdFlow(@RequestBody final String idFlow, 
            final HttpServletResponse  response) {
        try {
            final ConsultaList<UserLawyerEvaluator> aviableLawyersByFlowList = new ConsultaList<UserLawyerEvaluator>();
            aviableLawyersByFlowList.setList(this.userContractBusiness.findAviableLawyers(Integer.valueOf(idFlow)));
            return aviableLawyersByFlowList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
        return new ConsultaList<UserLawyerEvaluator>();
    }

    @RequestMapping (value = UrlConstants.SEARCH_AVAILABLE_EVALUATORS_BY_FLOW, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<UserLawyerEvaluator> findAviableEvaluatorsByIdFlow(@RequestBody final String idFlow, 
            final HttpServletResponse  response) {
        try {
            final ConsultaList<UserLawyerEvaluator> aviableEvaluatorsByFlowList = 
                    new ConsultaList<UserLawyerEvaluator>();
            aviableEvaluatorsByFlowList.setList(this.userContractBusiness.findAviableEvaluators(
                    Integer.valueOf(idFlow)));
            return aviableEvaluatorsByFlowList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
        return new ConsultaList<UserLawyerEvaluator>();
    }

//    @RequestMapping(value = UrlConstants.FIND_USER_ACTIVE_DIRECTORY, method = RequestMethod.POST)
//    @ResponseBody
//    public final User findUserActiveDirectory(@RequestBody final User user, final HttpServletResponse response,
//            final HttpServletRequest request) throws BusinessException {
//        try {
//            return this.ldapable.findUserActiveDirectory(user.getName());
//        } catch (BusinessException businessException) {
//            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
//            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
//        }
//        return null;
//    }

    @RequestMapping (value = UrlConstants.FIND_DECIDER_LAWYER_BY_FLOW, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<User> findDeciderLawyerByIdFlow(final HttpServletResponse  response) {
        try {
            final ConsultaList<User> deciderLawyerList = new ConsultaList<User>();
            deciderLawyerList.setList(this.userContractBusiness.findDeciderLawyer(this.session.getIdFlow()));
            return deciderLawyerList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
        return new ConsultaList<User>();
    }
}
