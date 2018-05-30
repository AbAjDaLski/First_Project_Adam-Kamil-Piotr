package pl.coderstrust.accounting.database.impl.file;

import pl.coderstrust.accounting.database.Database;
import pl.coderstrust.accounting.helpers.FileInvoiceHelper;
import pl.coderstrust.accounting.model.Invoice;
import pl.coderstrust.accounting.model.InvoiceEntry;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InFileDatabase implements Database {

  private String databaseFilePath;
  private String idFilePath;

  private final Map<Integer, Invoice> invoices = new HashMap<>();

  private int id = 0;

  public InFileDatabase(String databaseFilePath, String idFilePath) {
    if (databaseFilePath == null || "".equals(databaseFilePath)) {
      throw new IllegalArgumentException("Database filepath can't be empty");
    }
    if (idFilePath == null || "".equals(idFilePath)) {
      throw new IllegalArgumentException("ID filepath can't be empty");
    }
    this.databaseFilePath = databaseFilePath;
    this.idFilePath = idFilePath;
  }

  @Override
  public int saveInvoice(Invoice invoice) {
    int id = 0;
    try {
      id = FileInvoiceHelper.getAndIncrementLastId(idFilePath);
    } catch (IOException ioex) {
      throw new RuntimeException("IOException when opening idFile " + idFilePath, ioex);
    }
    Invoice invoiceToWrite = new Invoice(id, invoice.getIdentifier(), invoice.getIssuedDate(),
        invoice.getBuyer(), invoice.getSeller(), invoice.getEntries());
    try {
      FileInvoiceHelper.writeInvoiceToFile(invoiceToWrite, databaseFilePath);
    } catch (IOException ieox) {
      throw new RuntimeException(ieox);
    }
    return id;
  }

  @Override
  public void updateInvoice(Invoice invoice) {
  }

  @Override
  public void removeInvoice(int id) {
  }

  @Override
  public Invoice get(int id) {
    return null;
  }

  @Override
  public Collection<Invoice> find(Invoice searchParams, LocalDate issuedDateFrom,
      LocalDate issuedDateTo) {
    Set<Invoice> resultSchearching = new HashSet<>();
    // kolekcja przechowująca indywidualne nie powtarzające się elementy

    //wczytanie wszystkich faktur z pliku przesukanie ich wg
//resultSchearching.addAll()
    invoices = FileInvoiceHelper.readInvoicesFromFile(databaseFilePath);
//
    for (Invoice invoice : invoices.values()) {
      if (searchParams != null) {// faktur z konkretnym parametrem.
        if (searchParams.getId() != null) {
          if (searchParams.getId().equals(invoice.getId())) {
            resultSchearching.add(invoice);
            //zapisanie do pliku

            break;
          }
        }
        if (searchParams.getIdentifier() != null) {
          if (searchParams.getIdentifier().equals(invoice.getIdentifier())) {
            resultSchearching.add(invoice);
            break;
          }
        }
        if (searchParams.getBuyer() != null) {
          if (searchParams.getBuyer().equals(invoice.getBuyer())) {
            resultSchearching.add(invoice);
            break;
          }
        }
        if (searchParams.getSeller() != null) {
          if (searchParams.getSeller().equals(invoice.getSeller())) {
            resultSchearching.add(invoice);
            break;
          }
        }
        if (searchParams.getIssuedDate() != null) {
          if (searchParams.getIssuedDate().isEqual(invoice.getIssuedDate())) {
            resultSchearching.add(invoice);
            break;
          }
        }

        //czytanie wszystkich faktur
      }
      if (searchParams.getEntries() != null) {
        boolean found = false;
        for (InvoiceEntry entry : searchParams.getEntries()) {
          if (!found && invoice.getEntries().contains(entry)) {
            resultSchearching.add(invoice);
            found = true;
          }
        }
      }
      //szukanie faktur od jednej daty do 2 daty - z przedzoału
      //szukanie faktur od jednej daty do 2 daty - z przedzoału
      //szuanie faktur od daty do końca faktur
      //szukanie faktur do daty

    }
    return resultSchearching;


  }

  @Override
  public Collection<Invoice> getAll() {
    return null;
  }
}
