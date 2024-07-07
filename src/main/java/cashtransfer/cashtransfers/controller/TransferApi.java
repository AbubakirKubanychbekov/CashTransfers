package cashtransfer.cashtransfers.controller;

import cashtransfer.cashtransfers.entities.Transfer;
import cashtransfer.cashtransfers.services.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Abubakir Dev
 */
@Controller
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferApi {

    private final TransferService transferService;

    @GetMapping()
    String findAll(Model model){
        List<Transfer> allTransfers = transferService.findAll();
        model.addAttribute("transfers",allTransfers);
        return "transfer/findAllTransfers";
    }
}
