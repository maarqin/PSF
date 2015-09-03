/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.List;
import javax.faces.model.ListDataModel;
import models.Colegiado;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author a
 */
public class ColegiadoDataModel extends ListDataModel<Colegiado> implements SelectableDataModel<Colegiado> {

    public ColegiadoDataModel() {

    }

    public ColegiadoDataModel(List<Colegiado> data) {
        super(data);
    }

    @Override
    public Colegiado getRowData(String rowKey) {
        List<Colegiado> colegiados = (List<Colegiado>) getWrappedData();

        for (Colegiado col : colegiados) {
            return col;
        }

        return null;
    }

    @Override
    public Object getRowKey(Colegiado col) {
        return col;
    }
}
