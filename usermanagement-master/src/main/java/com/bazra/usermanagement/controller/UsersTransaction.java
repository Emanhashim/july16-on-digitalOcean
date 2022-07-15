package com.bazra.usermanagement.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bazra.usermanagement.model.Account;
import com.bazra.usermanagement.model.AgentInfo;
import com.bazra.usermanagement.model.Transaction;
import com.bazra.usermanagement.model.UserInfo;
import com.bazra.usermanagement.repository.AccountRepository;
import com.bazra.usermanagement.repository.AgentRepository;
import com.bazra.usermanagement.repository.TransactionRepository;
import com.bazra.usermanagement.request.Accountrequest;
import com.bazra.usermanagement.request.DepositRequest;
import com.bazra.usermanagement.request.TransferRequest;
import com.bazra.usermanagement.request.WithdrawRequest;
import com.bazra.usermanagement.response.AccountResponse;
import com.bazra.usermanagement.response.BalanceResponse;
import com.bazra.usermanagement.response.CommissionResponse;
import com.bazra.usermanagement.response.ResponseError;
import com.bazra.usermanagement.response.RevenueResponse;
import com.bazra.usermanagement.response.TotalNumberCommission;
import com.bazra.usermanagement.response.TotalResponseTransactionfee;
import com.bazra.usermanagement.response.TransactionResponse;
import com.bazra.usermanagement.service.AccountService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;



@RestController
@CrossOrigin("*")
@RequestMapping("/api/accounts")
@Api(value = "Wallet  User's Activity  Endpoint", description = "This EndPoint Activities For Bazra  Wallet User's Activity")
@ApiResponses(value ={
        @ApiResponse(code = 404, message = "web user that a requested page is not available "),
        @ApiResponse(code = 200, message = "The request was received and understood and is being processed "),
        @ApiResponse(code = 201, message = "The request has been fulfilled and resulted in a new resource being created "),
        @ApiResponse(code = 401, message = "The client request has not been completed because it lacks valid authentication credentials for the requested resource. "),
        @ApiResponse(code = 403, message = "Forbidden response status code indicates that the server understands the request but refuses to authorize it. ")

})
public class UsersTransaction {

    @Autowired
    private AccountService accountService;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AgentRepository agentRepository;
    Account agentAccount;
    
    public UserInfo getCurrentUser(@AuthenticationPrincipal UserInfo user) {
        return user;
    }
    @GetMapping("/all")
    @ApiOperation(value ="This EndPoint To Get All Users Who Use The Bazra Wallet")
    public List<Account> all(@RequestParam Optional<String> sortBy) {
        return accountRepository.findAll();
    }
    
    @PostMapping("/getaccount")
    @ApiOperation(value ="This EndPoint To Get All Users Who Use The Bazra Wallet")
    public ResponseEntity<?> getAccount(@RequestBody Accountrequest accountRequest) {
    	Account account = accountService.getAccount(accountRequest.getAccountNumber());
        return ResponseEntity.ok(new AccountResponse(account));
    }
    
    
    @PostMapping("/sendmoney")
    @ApiOperation(value ="This Allows User To Transfer Money From One Account To Other. Post Method")
    public ResponseEntity<?> sendMoney(@RequestBody TransferRequest transferBalanceRequest, Authentication authentication) {
    	System.out.println(authentication.getAuthorities());
        return accountService.sendMoney(transferBalanceRequest,authentication.getName());
    }
    
    @PostMapping("/withdraw")
//    @RolesAllowed("AGENT")
    @ApiOperation(value ="This EndPoint To WithDrawl Money From an Account. Post Method")
    public ResponseEntity<?> withdraw(@RequestBody  WithdrawRequest withdrawRequest,Authentication authentication) {
//        System.out.println(authentication.getAuthorities());
        return accountService.withdraw(withdrawRequest,authentication.getName());
    }
    
    @PostMapping("/deposit")
    @ApiOperation(value ="This EndPoint To Deposit Money From Account. Post Method")
    public ResponseEntity<?> deposit(@RequestBody DepositRequest depositRequest,Authentication authentication) {
        
        return accountService.Deposit(depositRequest,authentication.getName());
    }
    @PostMapping("/pay")
    @ApiOperation(value ="This EndPoint To Deposit Money From Account. Post Method")
    public ResponseEntity<?> pay(@RequestBody DepositRequest depositRequest,Authentication authentication) {
        
        return accountService.pay(depositRequest,authentication.getName());
    }
//    @GetMapping("/deposittransaction")
//    @ApiOperation(value ="This EndPoint To Get Deposit Transaction History. Get Method" )
//    public ResponseEntity<?> transactionDeposit(Authentication authentication) {
//
//
//    	Account account= accountRepository.findByAccountNumberEquals(authentication.getName()).get();
//       
//          if (account == null) {
//              return ResponseEntity.badRequest().body(new ResponseError("Invalid account"));
//          }
//          List<Transaction> deposittransactions =new ArrayList<Transaction>();
//          List<Transaction> transactions= transactionRepository.findByaccountNumberEquals(authentication.getName());
//          List<Transaction> transactionssList = transactionRepository.findByfromAccountNumberEquals(authentication.getName());
//          for (int i = 0; i < transactions.size(); i++) {
//        	  System.out.println(transactions.get(i).getTransaction_type());
//        	  if(transactions.get(i).getTransaction_type().matches("Deposit")) {
//        		  deposittransactions.add(transactions.get(i));
//        		  deposittransactions.add(transactionssList.get(i));
//        		  System.out.println(deposittransactions);
//        	  }
//			
//          	}
//           return ResponseEntity.ok(new TransactionResponse(deposittransactions));
////        return accountService.findall(transactionRequest.getAccountNumber());
//    }
    @GetMapping("/transaction")
    @ApiOperation(value ="This EndPoint To Get All Transaction History. Get Method" )
    public ResponseEntity<?> transaction(Authentication authentication) {


    	Account account= accountRepository.findByAccountNumberEquals(authentication.getName()).get();
       
          if (account == null) {
              return ResponseEntity.badRequest().body(new ResponseError("Invalid account"));
          }
           return ResponseEntity.ok(new TransactionResponse(transactionRepository.findByaccountNumberEquals(authentication.getName()),transactionRepository.findByfromAccountNumberEquals(authentication.getName())));
//        return accountService.findall(transactionRequest.getAccountNumber());
    }
    
    
    @GetMapping("/alltransactions")
    @ApiOperation(value ="This EndPoint To Get All Transaction History. Get Method" )
    public ResponseEntity<?> allTransactions() {

           return ResponseEntity.ok(new TransactionResponse(transactionRepository.findAll()));

    }

    @GetMapping("/balance")
    @ApiOperation(value ="This EndPoint Bring The Current Balance Get Method")
    public ResponseEntity<?> balance(Authentication authentication) {
    	Account account= accountRepository.findByAccountNumberEquals(authentication.getName()).get();

        if (account==null) {
            return ResponseEntity.badRequest().body(new ResponseError("Invalid account"));
        }
        BigDecimal balance = account.getBalance();
        
        
        return ResponseEntity.ok(new BalanceResponse(balance,"Your current balance equals "+balance,account.getUsername(),balance));
    }
    @GetMapping("/commission")
    @ApiOperation(value ="This EndPoint Bring The Current Balance Get Method")
    public ResponseEntity<?> commission(Authentication authentication) {
    	Account account= accountRepository.findByAccountNumberEquals(authentication.getName()).get();

        if (account==null) {
            return ResponseEntity.badRequest().body(new ResponseError("Invalid account"));
        }
        if(!account.getType().matches("AGENT")) {
        	return ResponseEntity.badRequest().body(new ResponseError("Not an agent account"));
        }
      
        BigDecimal commission = account.getCommission();
        System.out.println(commission.toString());
       
        return ResponseEntity.ok(new CommissionResponse(account.getCommission(),"Your total commission is "+commission,account.getUsername(),account.getCommission()));
    }
    
	private Sort.Direction getSortDirection(String direction) {
		if (direction.equals("asc")) {
			return Sort.Direction.ASC;
		} else if (direction.equals("desc")) {
			return Sort.Direction.DESC;
		}
		return Sort.Direction.ASC;
	}
	
    @GetMapping("/TransactionPaged")
    public ResponseEntity<Map<String, Object>> getAllTransactionPage(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "transactionId,desc") String[] sort){


        try{
            List<Sort.Order> orders = new ArrayList<Sort.Order>();

            if (sort[0].contains(",")) {

                for (String sortOrder : sort) {
                    String[] _sort = sortOrder.split(",");
                    orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
                }
            } else {
             
                orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
            }
            List<Transaction>  transactions = new ArrayList<>();
            List<Transaction>  transactionss = new ArrayList<>();
            Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));
            List<Transaction>  trans = new ArrayList<>();
            Page<Transaction> pageTuts;
            Page<Transaction> pageTutss = null;
            if(authentication.getName().isEmpty())
                pageTuts = transactionRepository.findAll(pagingSort);

            else {
                pageTuts = transactionRepository.findByfromAccountNumberEquals(authentication.getName(), pagingSort);
                pageTutss =transactionRepository.findByaccountNumberEquals(authentication.getName(), pagingSort);
            }
            System.out.println(pageTuts);
            transactions = pageTuts.getContent();
            transactionss =pageTutss.getContent();
            for (int i = 0; i < transactionss.size(); i++) {
            	transactionss.get(i).setUserID(transactions.get(0).getUserID());
			}
            trans.addAll(transactions);
            trans.addAll(transactionss);
            Map<String, Object> response = new HashMap<>();
            response.put("transactions", trans);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalTransaction", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);


        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/totalcommission")
    @ApiOperation(value ="This endPoint calculates total commission earned by an agent")
    public ResponseEntity<?> totalCommission(Authentication authentication) {
    	List<AgentInfo> agentInfo = agentRepository.findAll();
    	
    	BigDecimal totalCommission = BigDecimal.ZERO;
    	for (int i = 0; i < agentInfo.size(); i++) {
    		
    		agentAccount = accountRepository.findByAccountNumberEquals(agentInfo.get(i).getUsername()).get();
    		System.out.println(agentAccount.getCommission());
			totalCommission=totalCommission.add(agentAccount.getCommission());
			System.out.println(totalCommission);
		}
    	
    	return ResponseEntity.ok(new TotalNumberCommission(totalCommission,"Total transaction fee: "+totalCommission));
    }
    
    
    @GetMapping("/totaltransfee")
    @ApiOperation(value ="This EndPoint Bring The Current Balance Get Method")
    public ResponseEntity<?> totalTransactionfee(Authentication authentication) {
    	Account bazrAccount = accountRepository.findByAccountNumberEquals("091122334455").get();
    	BigDecimal tottransfee = bazrAccount.getBalance();
    	
    	
    	return ResponseEntity.ok(new TotalResponseTransactionfee(tottransfee,"Total transaction fee: "+tottransfee));
    }
    
    @GetMapping("/revenue")
    @ApiOperation(value ="This EndPoint Bring The Current Balance Get Method")
    public ResponseEntity<?> totalRevenue(Authentication authentication) {
    	Account bazrAccount = accountRepository.findByAccountNumberEquals("091122334455").get();
    	List<AgentInfo> agentsInfos = agentRepository.findAll();
    	
    	BigDecimal totalCommission = new BigDecimal(0);
    	BigDecimal netIncome = new BigDecimal(0);
    	for (int i = 0; i < agentsInfos.size(); i++) {
			
			totalCommission=totalCommission.add(accountRepository.findByAccountNumberEquals(agentsInfos.get(i).getUsername()).get().getCommission());
		}
    	System.out.println("t"+totalCommission);
    	netIncome= bazrAccount.getBalance().subtract(totalCommission);
    	if (bazrAccount.getBalance().compareTo(totalCommission)==-1) {
    		return ResponseEntity.ok(new RevenueResponse(netIncome,"You have a loss of: "+netIncome));
		} else {
			return ResponseEntity.ok(new RevenueResponse(netIncome,"You have a revenue of: "+netIncome));
		}

    }
    
}