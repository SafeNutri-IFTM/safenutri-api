package com.safenutri.safenutri_api.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.microsoft.playwright.*;

@Component
public class CrnValidation {

    public Boolean pesquisarCrn(String name, String crn, String inscricao) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
            );

            Long crnValue = processarCrn(crn);
            if (crnValue == 0) {
                return null;
            }

            Page page = browser.newPage();
            page.navigate("https://cnn.cfn.org.br/application/index/consulta-nacional");

            page.locator("input[name='nome']").fill(name);
            page.locator("input[name='registro']").fill(inscricao);
            page.locator("input.search").click();
            page.waitForSelector(String.format("div.item[data-value='%d']", crnValue));
            page.locator(String.format("div.item[data-value='%d']", crnValue)).click();

            page.locator("button:has-text('Pesquisar')").click();

            page.waitForSelector("table tbody tr", new Page.WaitForSelectorOptions().setTimeout(5000));

            Locator rows = page.locator("table tbody tr");
            List<CrnInfo> lista = new ArrayList<>();

            for (int i = 0; i < rows.count(); i++) {
                Locator row = rows.nth(i);
                Locator cols = row.locator("td");

                String nome = cols.nth(0).innerText();
                String CRN = cols.nth(2).innerText() + "/" + cols.nth(1).innerText();
                String situacao = cols.nth(3).innerText();

                CrnInfo info = new CrnInfo(CRN, nome, situacao);
                lista.add(info);
            }

            browser.close();
            return validarCrn(lista);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Long processarCrn(String CRN) {
        Long crnValue;
        switch (CRN) {
            case "CRN1":
                crnValue = 1L;
                break;
            case "CRN2":
                crnValue = 2L;
                break;
            case "CRN3":
                crnValue = 3L;
                break;
            case "CRN4":
                crnValue = 4L;
                break;
            case "CRN5":
                crnValue = 5L;
                break;
            case "CRN6":
                crnValue = 6L;
                break;
            case "CRN7":
                crnValue = 7L;
                break;
            case "CRN8":
                crnValue = 8L;
                break;
            case "CRN9":
                crnValue = 9L;
                break;
            case "CRN10":
                crnValue = 10L;
                break;
            case "CRN11":
                crnValue = 11L;
                break;
            default:
                crnValue = 0L;
        }
        return crnValue;
    }

    private Boolean validarCrn(List<CrnInfo> lista) {
        if (lista != null && !lista.isEmpty()) {
            for (CrnInfo cr : lista) {
                if(cr.getSituacao().equals("ATIVO")) {
                    return true;
                }
            }   
        }
        return false;
    }
}
