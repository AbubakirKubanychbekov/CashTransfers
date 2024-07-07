package cashtransfer.cashtransfers.controller;

import cashtransfer.cashtransfers.dto.response.PaginationResponse;
import cashtransfer.cashtransfers.entities.CashRegister;
import cashtransfer.cashtransfers.entities.User;
import cashtransfer.cashtransfers.services.CashRegisterService;
import cashtransfer.cashtransfers.services.UserService;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.constraints.Min;

import java.util.List;

/**
 * @author Abubakir Dev
 */
@Controller
@RequestMapping("/cash_registers")
public class CashRegisterApi {

    private final CashRegisterService cashRegisterService;
    private final UserService userService;


    public CashRegisterApi(CashRegisterService cashRegisterService, UserService userService) {
        this.cashRegisterService = cashRegisterService;
        this.userService = userService;
    }

    @PostMapping("/transfer")
    public String transferFunds(@RequestParam Long sourceId,
                                @RequestParam Long destinationId,
                                @RequestParam Double amount,
                                @RequestParam String currency,
                                @RequestParam String senderName,
                                @RequestParam String receiverName,
                                @RequestParam String senderPhone,
                                @RequestParam String receiverPhone,
                                @RequestParam String comment,
                                RedirectAttributes redirectAttributes) {
        try {
            if (sourceId.equals(destinationId)) {
                throw new IllegalArgumentException("Source and destination cannot be the same");
            }
            String transactionId = cashRegisterService.transfer(sourceId, destinationId, amount, currency, senderName, receiverName, senderPhone, receiverPhone, comment);
            redirectAttributes.addFlashAttribute("transferConfirmation", true);
            redirectAttributes.addFlashAttribute("transactionId", transactionId);
            return "redirect:/cash_registers";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Transfer failed: " + e.getMessage());
            return "redirect:/cash_registers";
        }
    }

    @PostMapping("/confirm-transfer")
    public String confirmTransfer(@RequestParam String transactionId,
                                  @RequestParam String transferCode,
                                  Model model) {
        try {
            cashRegisterService.confirmTransfer(transactionId, transferCode);
            model.addAttribute("confirmationMessage", "Transfer confirmed successfully.");
            return "redirect:/cash_registers";
        } catch (Exception e) {
            model.addAttribute("error", "Confirmation failed: " + e.getMessage());
            return "redirect:/cash_registers";
        }
    }

    @GetMapping()
    @PermitAll
    String findAll(Model model) {
        List<CashRegister> allCashRegisters = cashRegisterService.findAll();
        model.addAttribute("cashRegisters", allCashRegisters);
        return "cashRegister/findAll";
    }


    @GetMapping("/create")
    @PermitAll
    String createCashRegister(Model model) {
        model.addAttribute("newCashRegister", new CashRegister());
        return "cashRegister/savePage";
    }

    @PostMapping("/save")
    @PermitAll
    public String saveCashRegister(@ModelAttribute("newCashRegister") CashRegister cashRegister,
                                   @RequestParam("email") String email) {
        User user = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        cashRegister.setUser(user);
        cashRegisterService.save(cashRegister);
        return "redirect:/cash_registers";
    }


    @GetMapping("/update/{cashRegisterId}")
    String updatePage(@PathVariable Long cashRegisterId, Model model) {
        model.addAttribute("currentCashRegister", cashRegisterService.findById(cashRegisterId));
        return "cashRegister/updatePage";
    }

    @PostMapping("/edit/{cashRegisterId}")
    String editCashRegister(@ModelAttribute CashRegister newCashRegister,
                            @PathVariable Long cashRegisterId) {
        cashRegisterService.update(cashRegisterId, newCashRegister);
        return "redirect:/cash_registers";
    }

    @GetMapping("/delete/{cashRegisterId}")
    String deleteCashRegister(@PathVariable Long cashRegisterId) {
        cashRegisterService.deleteById(cashRegisterId);
        return "redirect:/cash_registers";
    }

    @GetMapping("/search")
    public String searchCashRegisters(@RequestParam(required = false) Long cashRegisterId,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) Double balance,
                                      @RequestParam(required = false) String urlImage,
                                      Model model) {
        List<CashRegister> cashRegisters = cashRegisterService.findByCriteria(name, balance);

        model.addAttribute("cashRegisterId", cashRegisterId);
        model.addAttribute("name", name);
        model.addAttribute("balance", balance);
        model.addAttribute("urlImage", urlImage);
        model.addAttribute("cashRegisters", cashRegisters);

        return "cashRegister/findAll";
    }

    @GetMapping("/pagination")
    public String paginationResponse(
            @RequestParam @Min(1) int currentPage,
            @RequestParam @Min(1) int pageSize,
            Model model
    ) {
        if (currentPage < 1 || pageSize < 1) {
            return "redirect:/error";
        }

        PaginationResponse response = cashRegisterService.getAllPagination(currentPage, pageSize);
        model.addAttribute("cashRegisters", response.getCashRegisters());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", response.getTotalPages());
        return "cashRegister/findAll";
    }
}