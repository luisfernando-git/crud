package br.com.javamagazine.jee.crud.common.cliente.services;

import br.com.javamagazine.jee.crud.cliente.dao.ClienteDao;
import br.com.javamagazine.jee.crud.cliente.entity.Cliente;
import br.com.javamagazine.jee.crud.cliente.entity.ClienteTelefone;
import br.com.javamagazine.jee.crud.cliente.entity.filter.ClienteFilter;
import br.com.javamagazine.jee.crud.cliente.exception.ClienteExistenteException;
import br.com.javamagazine.jee.crud.cliente.exception.NenhumTelefoneInformadoException;
import br.com.javamagazine.jee.crud.cliente.services.ClienteServices;
import br.com.javamagazine.jee.crud.cliente.services.impl.ClienteServicesImpl;
import br.com.javamagazine.jee.crud.common.dao.DaoTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.*;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class TestClienteServices {

    private static final DateFormat FORMATO_DATA = new SimpleDateFormat("dd/MM/yyyy");

    @Inject
    private ClienteServices clienteServices;

    @Inject
    private DaoTest daoTest;

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "testCliente.war")
                .addClasses(Cliente.class, ClienteTelefone.class, ClienteFilter.class)
                .addClasses(ClienteExistenteException.class, NenhumTelefoneInformadoException.class)
                .addClasses(ClienteServices.class, ClienteServicesImpl.class, ClienteDao.class)
                .addClasses(DaoTest.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource("test-ds.xml");
    }

    @Before
    public void setUpTest() {
        daoTest.deleteAll(Cliente.class.getSimpleName());
    }
    
    @Test
    public void testAdd() throws Exception, NenhumTelefoneInformadoException, ClienteExistenteException {
        Long idCliente = clienteServices.save(getCliente()).getId();
        Cliente cliente = clienteServices.findById(idCliente);
        assertNotNull(cliente);
        assertEquals("Luis Fernando", cliente.getNome());
        assertEquals(getDate("15/02/1999"), cliente.getDataNascimento());
        assertEquals("goncalveslf99@gmail.com", cliente.getEmail());
        assertEquals(2, cliente.getTelefones().size());
    }
    
    @Test
    public void testAddEmailDuplicado() throws Exception {
        try {
            clienteServices.save(getCliente());
        }
        catch (ClienteExistenteException | NenhumTelefoneInformadoException e) {
            fail("Nao deveria ter dado falha");
        }
        try {
            clienteServices.save(getCliente());
            fail("Deveria ter dado falha");
        }
        catch (ClienteExistenteException | NenhumTelefoneInformadoException e) {

        }
    }

    @Test
    public void testAddSemTelefone() throws Exception {
        Cliente cliente = new Cliente("Bill", getDate("15/02/1999"),
                "goncalveslf99@gmail.com");
        try {
            clienteServices.save(cliente);
            fail("Deveria ter dado falha");
        }
        catch (NenhumTelefoneInformadoException | ClienteExistenteException e) {}
        cliente.addTelefone(new ClienteTelefone("19-1111-1111", "Residencial"));
        try {
            clienteServices.save(cliente);
        }
        catch (NenhumTelefoneInformadoException | ClienteExistenteException e) {
            fail("Nao deveria ter dado falha");
        }
    }
    
    @Test
    public void testUpdate() throws Exception, NenhumTelefoneInformadoException, ClienteExistenteException {
        Long idCliente = clienteServices.save(getCliente()).getId();
        Cliente cliente = clienteServices.findById(idCliente);
        
        // verifica os dados antes da alteracao
        assertEquals("Luis Fernando", cliente.getNome());
        assertEquals(getDate("15/02/1999"), cliente.getDataNascimento());
        assertEquals("goncalveslf99@gmail.com", cliente.getEmail());
        assertEquals(2, cliente.getTelefones().size());
        
        // altera os dados
        cliente.setDataNascimento(getDate("11/11/1981"));
        cliente.setEmail("luis@outrosite.com");
        cliente.addTelefone(new ClienteTelefone("19-8888-9999", "Comercial"));
        clienteServices.save(cliente);
        
        // verifica os dados apos a alteracao
        cliente = clienteServices.findById(idCliente);
        assertEquals("Luis Fernando", cliente.getNome());
        assertEquals(getDate("11/11/1981"), cliente.getDataNascimento());
        assertEquals("luis@outrosite.com", cliente.getEmail());
        assertEquals(3, cliente.getTelefones().size());
        
        // verifica se deixa alterar o cliente sem alterar o e-mail
        try {
            cliente.setDataNascimento(getDate("12/11/1980"));
            clienteServices.save(cliente);
        }
        catch (ClienteExistenteException e) {
            fail("Deveria ter deixado alterar o cliente");
        }
        // adiciona outro cliente e verifica que o email do cliente atual nao
        // pode ser alterado para esse outro
        clienteServices.save(getCliente());
        cliente.setEmail("goncalveslf99@gmail.com");
        try {
            clienteServices.save(cliente);
            fail("Nao deveria ter deixado alterar o email do cliente");
        }
        catch (ClienteExistenteException e) {}
    }

    @Test
    public void testRemove() throws Exception, NenhumTelefoneInformadoException, ClienteExistenteException {
        Long idCliente = clienteServices.save(getCliente()).getId();
        assertNotNull(clienteServices.findById(idCliente));
        clienteServices.remove(idCliente);
        assertNull(clienteServices.findById(idCliente));
    }
    
    @Test
    public void testFindByFilter() throws Exception, NenhumTelefoneInformadoException, ClienteExistenteException {
        Cliente martin = new Cliente("Martin", getDate("08/02/1974"),
                "martin@site.com");
        martin.addTelefone(new ClienteTelefone("19-3211-1111", "Contato"));
        Cliente john = new Cliente("John", getDate("12/04/1983"), "john@site.com");
        john.addTelefone(new ClienteTelefone("19-8211-1111", "Celular"));
        Cliente mark = new Cliente("Mark", getDate("15/05/1984"), "mark@site.com");
        mark.addTelefone(new ClienteTelefone("19-9211-1111", "Celular"));
        Cliente bill = new Cliente("Bill", getDate("10/10/1980"), "bill@site.com");
        bill.addTelefone(new ClienteTelefone("19-1111-1111", "Residencial"));
        clienteServices.save(martin);
        clienteServices.save(john);
        clienteServices.save(mark);
        clienteServices.save(bill);
        List<Cliente> clientes = clienteServices.findByFilter(null);
        // verifica se todos foram encontrados e na ordem esperada
        assertEquals(4, clientes.size());
        assertNomes(Arrays.asList("Bill", "John", "Mark", "Martin"), clientes);
        clientes = clienteServices.findByFilter(new ClienteFilter("Mar"));
        // verifica se todos foram encontrados e na ordem esperada
        assertEquals(2, clientes.size());
        assertNomes(Arrays.asList("Mark", "Martin"), clientes);
    }
    
    private void assertNomes(List<String> nomesEsperados, List<Cliente> clientes) {
        assertEquals(nomesEsperados.size(), clientes.size());
        for (int i = 0; i < nomesEsperados.size(); i++) {
            assertEquals(nomesEsperados.get(i), clientes.get(i).getNome());
        }
    }
    
    private Date getDate(String dataStr) {
        try {
            return FORMATO_DATA.parse(dataStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private Cliente getCliente() {
        Cliente cliente = new Cliente("Luis Fernando", getDate("15/02/1999"),
                "goncalveslf99@gmail.com");
        cliente.addTelefone(new ClienteTelefone("19-1111-1111", "Residencial"));
        cliente.addTelefone(new ClienteTelefone("19-9111-1111", "Celular"));
        return cliente;
    }

}