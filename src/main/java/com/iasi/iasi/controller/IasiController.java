package com.iasi.iasi.controller;

import com.iasi.iasi.model.Consumo;
import com.iasi.iasi.model.Empresa;
import com.iasi.iasi.model.Equipamento;
import com.iasi.iasi.model.Usuario;
import com.iasi.iasi.repository.ConsumoRepository;
import com.iasi.iasi.repository.EmpresaRepository;
import com.iasi.iasi.repository.EquipamentoRepository;
import com.iasi.iasi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


//@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class IasiController {
    @Autowired
    EmpresaRepository empresaRepository;

    @Autowired
    EquipamentoRepository equipamentoRepository;

    @Autowired
    ConsumoRepository consumoRepository;

    @GetMapping("/empresas")
    public ResponseEntity<List<Empresa>> getAllEmpresas(@RequestParam(required = false) String nome) {
        try {
            List<Empresa> empresas = new ArrayList<Empresa>();

            if (nome == null)
                empresaRepository.findAll().forEach(empresas::add);
            else
                empresaRepository.findByNameContaining(nome).forEach(empresas::add);

            if (empresas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(empresas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/empresas/{id}")
    public ResponseEntity<Empresa> getEmpresaById(@PathVariable("id") long id) {
        Empresa empresa = empresaRepository.findById(id);

        if (empresa != null) {
            return new ResponseEntity<>(empresa, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/empresas")
    public ResponseEntity<String> createEmpresa(@RequestBody Empresa empresa) {
        try {
            empresaRepository.save(new Empresa(empresa.getNome(), empresa.getSetorIndustrial(), empresa.getLocalizacao(), empresa.getTipo(), empresa.getConformidadeRegular()));
            return new ResponseEntity<>("Empresa foi criado com sucesso.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/empresas/{id}")
    public ResponseEntity<String> updateEmpresa(@PathVariable("id") long id, @RequestBody Empresa empresa) {
        Empresa _empresa = empresaRepository.findById(id);

        if (_empresa != null) {
            _empresa.setId(id);
            _empresa.setNome(empresa.getNome());
            _empresa.setSetorIndustrial(empresa.getSetorIndustrial());
            _empresa.setLocalizacao(empresa.getLocalizacao());
            _empresa.setTipo(empresa.getTipo());
            _empresa.setConformidadeRegular(empresa.getConformidadeRegular());

            empresaRepository.update(_empresa);
            return new ResponseEntity<>("Empresa foi atualizado com sucesso.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Não foi encontrada Empresa com id=" + id, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/empresas/{id}")
    public ResponseEntity<String> deleteEmpresa(@PathVariable("id") long id) {
        try {
            int result = empresaRepository.deleteById(id);
            if (result == 0) {
                return new ResponseEntity<>("Não foi encontrada Empresa com id=" + id, HttpStatus.OK);
            }
            return new ResponseEntity<>("Empresa foi deletada com sucesso.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Não é possível deletar Empresa.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/empresas")
    public ResponseEntity<String> deleteAllEmpresas() {
        try {
            int numRows = empresaRepository.deleteAll();
            return new ResponseEntity<>("Deletadas " + numRows + " Empresa(s) com sucesso.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Não é possível deletar Empresa.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/empresas/{id}/equipamentos")
    public ResponseEntity<List<Equipamento>> getEquipamentosByEmpresaId(@PathVariable("id") long id) {
        List<Equipamento> equipamentos = equipamentoRepository.findByEmpresaId(id);
        if (equipamentos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(equipamentos, HttpStatus.OK);
    }

    @DeleteMapping("/empresas/{idEmpresa}/equipamentos/{idEquipamento}")
    public ResponseEntity<String> removeEquipamentoFromEmpresa(@PathVariable("idEmpresa") long idEmpresa, @PathVariable("idEquipamento") long idEquipamento) {
        // Verifique se a empresa existe
        Empresa empresa = empresaRepository.findById(idEmpresa);
        if (empresa == null) {
            return new ResponseEntity<>("Empresa não encontrada com id=" + idEmpresa, HttpStatus.NOT_FOUND);
        }

        // Verifique se o equipamento está associado à empresa
        Equipamento equipamento = equipamentoRepository.findById(idEquipamento);
        //if (equipamento == null || equipamento.getEmpresa().getId() != idEmpresa) {
            //return new ResponseEntity<>("Equipamento não encontrado ou não associado à empresa com id=" + idEmpresa, HttpStatus.NOT_FOUND);
        //}
        if (equipamento == null) {
            return new ResponseEntity<>("Equipamento não encontrado ou não associado à empresa com id=" + idEmpresa, HttpStatus.NOT_FOUND);
        }

        // Verifique se existem consumos associados a esse equipamento
        List<Consumo> consumos = consumoRepository.findByEquipamentoId(idEquipamento);
        if (!consumos.isEmpty()) {
            return new ResponseEntity<>("Não é possível excluir o equipamento pois há consumos associados. Remova os consumos primeiro.", HttpStatus.BAD_REQUEST);
        }

        // Remova o equipamento
        equipamentoRepository.deleteById(idEquipamento);
        return new ResponseEntity<>("Equipamento removido com sucesso da empresa com id=" + idEmpresa, HttpStatus.OK);
    }

    @GetMapping("/equipamentos")
    public ResponseEntity<List<Equipamento>> getAllEquipamentos(@RequestParam(required = false) String nomeEquipamento) {
        try {
            List<Equipamento> equipamentos = new ArrayList<Equipamento>();

            if (nomeEquipamento == null)
                equipamentoRepository.findAll().forEach(equipamentos::add);
            else
                equipamentoRepository.findByNameContaining(nomeEquipamento).forEach(equipamentos::add);

            if (equipamentos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(equipamentos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/empresas/{id}/equipamentos")
    public ResponseEntity<String> createEquipamento(@PathVariable long id, @RequestBody Equipamento equipamento) {
        try {
            Empresa empresa = empresaRepository.findById(id);
            if (empresa == null) {
                return new ResponseEntity<>("Empresa com o ID especificado não encontrado.", HttpStatus.NOT_FOUND);
            }
            //equipamento.setEmpresa(empresa);
            // Aqui você está tentando salvar um novo Equipamento sem especificar o ID da empresa
            //equipamentoRepository.save(new Equipamento(equipamento.getNomeEquipamento(), equipamento.getTipoEquipamento(), equipamento.getLocalizacaoEquipamento(), equipamento.getDataInstalacaoEquipamento(), equipamento.getEstadoEquipamento(), equipamento.getEmpresa()));
            equipamentoRepository.save(new Equipamento(equipamento.getNomeEquipamento(), equipamento.getTipoEquipamento(), equipamento.getLocalizacaoEquipamento(), equipamento.getDataInstalacaoEquipamento(), equipamento.getEstadoEquipamento()));
            return new ResponseEntity<>("Equipamento foi adicionado com sucesso.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/equipamentos/{idEquipamento}/consumo")
    public ResponseEntity<String> createConsumo(@PathVariable long idEquipamento, @RequestBody Consumo consumo) {
        try {
            Equipamento equipamento = equipamentoRepository.findById(idEquipamento);
            if (equipamento == null) {
                return new ResponseEntity<>("Equipamento com o ID especificado não encontrado.", HttpStatus.NOT_FOUND);
            }
            consumo.setEquipamento(equipamento);
            consumoRepository.save(new Consumo(consumo.getEquipamento(), consumo.getDataConsumo(), consumo.getQuantidadeConsumo(), consumo.getTipoEnergiaConsumo(), consumo.getEmissaoGasConsumo()));
            return new ResponseEntity<>("Consumo foi adicionado com sucesso.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

        @GetMapping("/equipamentos/{id}/consumo")
    public ResponseEntity<List<Consumo>> getConsumoByEquipamentoId(@PathVariable("id") long id) {
        List<Consumo> consumos = consumoRepository.findByEquipamentoId(id);
        if (consumos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(consumos, HttpStatus.OK);
    }

    @DeleteMapping("/equipamentos/{idEquipamento}/consumo/{idConsumo}")
    public ResponseEntity<String> removeConsumoFromEquipamento(@PathVariable("idEquipamento") long idEquipamento, @PathVariable("idConsumo") long idConsumo) {
        Equipamento equipamento = equipamentoRepository.findById(idEquipamento);
        if (equipamento == null) {
            return new ResponseEntity<>("Equipamento não encontrado com id=" + idEquipamento, HttpStatus.NOT_FOUND);
        }
        Consumo consumo = consumoRepository.findById(idConsumo);
        if (consumo == null || consumo.getEquipamento().getIdEquipamento() != idEquipamento) {
            return new ResponseEntity<>("Consumo não encontrado ou não associado ao equipamento com id=" + idEquipamento, HttpStatus.NOT_FOUND);
        }
        consumoRepository.deleteById(idConsumo);
        return new ResponseEntity<>("Consumo removido com sucesso do equipamento com id=" + idEquipamento, HttpStatus.OK);
    }

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/usuarios/registrar")
    public ResponseEntity<String> registrarUsuario(@RequestBody Usuario usuario) {
        usuarioService.registrarUsuario(usuario);
        return new ResponseEntity<>("Usuário registrado com sucesso.", HttpStatus.CREATED);
    }

    @PostMapping("/usuarios/login")
    public ResponseEntity<String> login(@RequestParam String nomeUsuario, @RequestParam String senha) {
        boolean loginSucesso = usuarioService.verificarCredenciais(nomeUsuario, senha);
        if (loginSucesso) {
            return new ResponseEntity<>("Login bem-sucedido.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Credenciais inválidas.", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/empresas/{id}/consumo")
    public ResponseEntity<Float> getConsumoTotalByEmpresaId(@PathVariable("id") long id) {
        Iterable<Equipamento> equipamentos = equipamentoRepository.findByEmpresaId(id);
        float totalConsumo = 0f;
        for (Equipamento equipamento : equipamentos) {
            Iterable<Consumo> consumos = consumoRepository.findByEquipamentoId(equipamento.getIdEquipamento());
            for (Consumo consumo : consumos) {
                totalConsumo += consumo.getQuantidadeConsumo();
            }
        }
        return ResponseEntity.ok(totalConsumo);
    }

}

