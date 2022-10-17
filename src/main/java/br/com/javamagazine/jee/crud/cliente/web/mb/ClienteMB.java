package br.com.javamagazine.jee.crud.cliente.web.mb;

import br.com.javamagazine.jee.crud.cliente.entity.Cliente;
import br.com.javamagazine.jee.crud.cliente.entity.ClienteTelefone;
import br.com.javamagazine.jee.crud.cliente.entity.filter.ClienteFilter;
import br.com.javamagazine.jee.crud.cliente.exception.ClienteExistenteException;
import br.com.javamagazine.jee.crud.cliente.exception.NenhumTelefoneInformadoException;
import br.com.javamagazine.jee.crud.cliente.services.ClienteServices;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.lang.*;

@Named
@SessionScoped
public class ClienteMB implements Serializable {

    private static final long serialVersionUID = 6920835789315490285L;

    @Inject
    private ClienteServices clienteServices;

    private Cliente cliente = new Cliente();

    private Long idCliente;

    private List<Cliente> clientes;

    private String nomeBuscar;

    private ClienteTelefone telefoneAdd = new ClienteTelefone();

    private List<ClienteTelefone> telefones;

    private ResourceBundle bundle;

    @PostConstruct
    public void init() {
        this.bundle = ResourceBundle.getBundle("messages", getFacesContext().getViewRoot().getLocale());
    }

    public List<Cliente> getClientes() {
        if (clientes == null) {
            clientes = clienteServices.findByFilter(new ClienteFilter(nomeBuscar));
        }
        return clientes;
    }

    public void load() {
        if (!getFacesContext().isPostback()) {
            if (idCliente != null) {
                cliente = clienteServices.findById(idCliente);
                if (cliente == null) {
                    redirect("list.xhtml");
                }
            } else {
                cliente = new Cliente();
            }
        }
    }

    public String save() {
        try {
            clienteServices.save(cliente);
            addMessageFromBundleInFlash(FacesMessage.SEVERITY_INFO, "label.cliente.salvo");
            limpar();
            return "list?faces-redirect=true";
        }
        catch (ClienteExistenteException e) {
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", bundle.getString("label.cliente-existente")));
        }
        catch (NenhumTelefoneInformadoException e1) {
            getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", bundle.getString("label.nenhum-telefone-informado")));
        }
        return null;
    }

    public String remove() {
        clienteServices.remove(cliente.getId());
        addMessageFromBundleInFlash(FacesMessage.SEVERITY_INFO, "label.cliente.excluido");
        limpar();
        return "list?faces-redirect=true";
    }

    public void buscar() {
        limpar();
    }

    public String cancelar() {
        limpar();
        return "list?faces-redirect=true";
    }
    public void addTelefone() {
        if (telefoneAdd.getTelefone() == null && telefoneAdd.getTelefone().isEmpty()) {
            getFacesContext().addMessage(
                    "formCliente:txtTelefone",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "", bundle
                            .getString("label.telefone-nao-informado")));
        }
        if (telefoneAdd.getDescricao() == null && telefoneAdd.getDescricao().isEmpty()) {
            getFacesContext().addMessage(
                    "formCliente:txtDescricao",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "", bundle
                            .getString("label.descricao-nao-informada")));
        }
        if (getFacesContext().getMessageList().size() == 0) {
            if (cliente.addTelefone(telefoneAdd)) {
                telefoneAdd = new ClienteTelefone();
                telefones = null;
            }
            else {
                getFacesContext().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "", bundle
                                .getString("label.telefone-existente")));
            }
        }
    }

    public void removeTelefone(ClienteTelefone clienteTelefone) {
        cliente.getTelefones().remove(clienteTelefone);
        telefones = null;
    }

    public List<ClienteTelefone> getTelefones() {
        if (telefones == null) {
            telefones = new ArrayList<ClienteTelefone>(cliente.getTelefones());
        }
        return telefones;
    }

    private void redirect(String pagina) {
        try {
            getFacesContext().getExternalContext().redirect(pagina);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMessageFromBundleInFlash(FacesMessage.Severity severidade, String chave) {
        Flash flash = getFacesContext().getExternalContext().getFlash();
        flash.setKeepMessages(true);
        getFacesContext().addMessage(null,
                new FacesMessage(severidade, null, bundle.getString(chave)));
    }
    private FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    private void limpar() {
        idCliente = null;
        cliente = new Cliente();
        clientes = null;
        telefoneAdd = new ClienteTelefone();
        telefones = null;
    }

    public String getNomeBuscar() {
        return nomeBuscar;
    }

    public void setNomeBuscar(String nomeBuscar) {
        this.nomeBuscar = nomeBuscar;
    }
}