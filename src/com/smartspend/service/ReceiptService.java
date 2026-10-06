package com.smartspend.service;
import java.nio.file.*;import java.util.regex.*;import com.smartspend.model.*;import java.time.*;
public class ReceiptService {public Transaction parse(Path image){String n=image.getFileName().toString().toLowerCase();String merchant=n.replace(".png","").replace(".jpg","").replace(".jpeg","").replace("_"," ");Transaction t=new Transaction();t.setDescription(merchant.isBlank()?"Receipt expense":merchant);t.setTransactionDate(LocalDate.now());t.setAmount(0);t.setNotes("Demo parser: replace ReceiptService with OCR provider for real extraction.");return t;}}
