package pl.coderstrust.accounting.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.accounting.logic.InvoiceService;
import pl.coderstrust.accounting.model.Invoice;
import pl.coderstrust.accounting.model.validator.InvoiceValidator;
import pl.coderstrust.accounting.model.validator.exception.InvoiceValidationException;

import java.time.LocalDate;
import java.util.Collection;

@RestController
@RequestMapping("/invoices")
@Api(value = "/{invoices}", description = "Operations on invoices")
public class InvoiceController {

  private static Logger logger = LoggerFactory.getLogger(InvoiceController.class);

  private InvoiceService invoiceService;
  private InvoiceValidator invoiceValidator;

  public InvoiceController(InvoiceService invoiceService, InvoiceValidator invoiceValidator) {
    this.invoiceService = invoiceService;
    this.invoiceValidator = invoiceValidator;
  }

  @ApiOperation(value = "Find all invoices",
      notes = "Method returns list of all invoices")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "All invoices"),
      @ApiResponse(code = 401, message = "Access unauthorized "),
      @ApiResponse(code = 403, message = "Access forbidden "),
      @ApiResponse(code = 404, message = "List of invoices is empty")})
  @GetMapping
  public Collection<Invoice> findInvoices() {
    logger.info("Received find invoices request");
    return invoiceService.getAll();
  }

  @ApiOperation(value = "Find invoices by id",
      notes = "Method returns invoice with in demand id")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Found invoice"),
      @ApiResponse(code = 403, message = "Access forbidden "),
      @ApiResponse(code = 401, message = "Access unauthorized "),
      @ApiResponse(code = 400, message = "Bad format date, use number"),
      @ApiResponse(code = 404, message = "Invoice is not exist")})
  @GetMapping("/{id}")
  public Invoice findSingleInvoice(
      @PathVariable(name = "id", required = true) int id) {
    return invoiceService.findById(id);
  }

  @ApiOperation(value = "Find invoices from the date range",
      notes = "Method returns all invoices from the date range")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Found invoices"),
      @ApiResponse(code = 400, message = "Bad format date, insert bad format, use format YYYY-MM-DD"),
      @ApiResponse(code = 401, message = "Access unauthorized "),
      @ApiResponse(code = 403, message = "Access forbidden "),
      @ApiResponse(code = 404, message = "Invoices is not exist")})
  @GetMapping("{dateFrom}/{dateTo}")
  public Collection<Invoice> findSingleIvoiceByDateRange(
      @PathVariable("dateFrom") @DateTimeFormat(iso = ISO.DATE) LocalDate dateFrom,
      @PathVariable("dateTo") @DateTimeFormat(iso = ISO.DATE) LocalDate dateTo) {
    logger.info("Received find invoices withing set date range request");
    return invoiceService
        .findInvoices(null,
            dateFrom, dateTo);
  }

  @ApiOperation(value = "Add invoice",
      notes = "Method add new invoice")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Added new invoice"),
      @ApiResponse(code = 400, message = "Bad format invoice, use format json"),
      @ApiResponse(code = 401, message = "Access unauthorized "),
      @ApiResponse(code = 403, message = "Access forbidden ")})
  @PostMapping
  public ResponseEntity<?> saveInvoice(@RequestBody Invoice invoice) {
    logger.info("Received save invoice request");
    Collection<InvoiceValidationException> validationErrors = invoiceValidator
        .validateInvoiceForSave(invoice);
    if (validationErrors.isEmpty()) {
      return ResponseEntity.ok(invoiceService.saveInvoice(invoice));
    }
    return ResponseEntity.badRequest().body(validationErrors);
  }

  @ApiOperation(value = "Remove invoice by id",
      notes = "Method remove exist invoice")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Removed invoice"),
      @ApiResponse(code = 400, message = "Bad format date, use number"),
      @ApiResponse(code = 401, message = "Access unauthorized "),
      @ApiResponse(code = 403, message = "Access forbidden "),
      @ApiResponse(code = 500, message = "Didn't remove, invoice is not exist")})
  @DeleteMapping
  public void removeInvoice(@RequestBody int id) {
    logger.info("Received remove invoice request");
    invoiceService.removeInvoice(id);
  }

  @ApiOperation(value = "Update invoice by id",
      notes = "Method update exist invoice")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Updated invoice"),
      @ApiResponse(code = 401, message = "Access unauthorized "),
      @ApiResponse(code = 403, message = "Access forbidden "),
      @ApiResponse(code = 400, message = "insert bad format, use format YYYY-MM-DD"),
      @ApiResponse(code = 500, message = "Didn't update, invoice is not exist")})
  @PutMapping("/{id}")
  public void updateInvoice(@PathVariable int id, @RequestBody Invoice invoice) {
    logger.info("Received update invoice request");
    invoiceService.updateInvoice(invoice);
  }
}