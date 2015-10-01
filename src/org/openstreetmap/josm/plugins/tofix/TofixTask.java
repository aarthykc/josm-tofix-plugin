package org.openstreetmap.josm.plugins.tofix;

import java.util.List;
import javax.swing.JOptionPane;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.downloadtasks.DownloadOsmTask;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.plugins.tofix.bean.AccessToTask;
import org.openstreetmap.josm.plugins.tofix.bean.items.Item;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemKeeprightBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemKrakatoaBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemNycbuildingsBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemSmallcomponents;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemStrava;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemTigerdeltaBean;
import org.openstreetmap.josm.plugins.tofix.bean.items.ItemUnconnectedBean;
import org.openstreetmap.josm.plugins.tofix.controller.ItemController;
import org.openstreetmap.josm.plugins.tofix.layer.TofixLayer;
import org.openstreetmap.josm.plugins.tofix.util.Download;
import static org.openstreetmap.josm.tools.I18n.tr;

/**
 *
 * @author ruben
 */
public class TofixTask {

    ItemController itemController = new ItemController();
    Bounds bounds = null;
    DownloadOsmTask downloadOsmTask = new DownloadOsmTask();
    Node node = null;
    TofixLayer tofixLayer = new TofixLayer("Tofix-layer");

    public AccessToTask work(Item item, AccessToTask accessToTask, double size) { //size to download

        if (accessToTask.getTask_source().equals("unconnected")) {
            accessToTask = work_unconnected(item.getItemUnconnectedBean(), accessToTask, size);
        }
        if (accessToTask.getTask_source().equals("keepright")) {
            accessToTask = work_keepright(item.getItemKeeprightBean(), accessToTask, size);
        }
        if (accessToTask.getTask_source().equals("tigerdelta")) {
            accessToTask = work_tigerdelta(item.getItemTigerdeltaBean(), accessToTask, size);
        }
        if (accessToTask.getTask_source().equals("nycbuildings")) {
            accessToTask = work_nycbuildings(item.getItemNycbuildingsBean(), accessToTask, size);
        }
        if (accessToTask.getTask_source().equals("krakatoa")) {
            accessToTask = work_krakatoa(item.getItemKrakatoaBean(), accessToTask, size);
        }
        if (accessToTask.getTask_source().equals("strava")) {
            accessToTask = work_strava(item.getItemStrava(), accessToTask, size);
        }
        if (accessToTask.getTask_source().equals("components")) {
            accessToTask = work_smallcomponents(item.getItemSmallcomponents(), accessToTask, size);
        }

        return accessToTask;
    }

    private AccessToTask work_unconnected(ItemUnconnectedBean itemUnconnectedBean, AccessToTask accessToTask, double size) {
        accessToTask.setKey(itemUnconnectedBean.getKey());
        node = itemUnconnectedBean.getValue().get_node();
        bounds = new Bounds(node.getCoor().toBBox(size).toRectangle());
        TofixDraw.draw_Node(tofixLayer, node.getCoor());
        Download.Download(downloadOsmTask, bounds, itemUnconnectedBean.getValue().getNode_id(),accessToTask.isShow_pm());
        return accessToTask;
    }

    private AccessToTask work_keepright(ItemKeeprightBean itemKeeprightBean, AccessToTask accessToTask, double size) {
        accessToTask.setKey(itemKeeprightBean.getKey());
        node = itemKeeprightBean.getValue().get_node();
        bounds = new Bounds(node.getCoor().toBBox(size).toRectangle());
        TofixDraw.draw_Node(tofixLayer, node.getCoor());
        Download.Download(downloadOsmTask, bounds, itemKeeprightBean.getValue().getObject_id(),accessToTask.isShow_pm());
        return accessToTask;

    }

    private AccessToTask work_nycbuildings(ItemNycbuildingsBean itemNycbuildingsBean, AccessToTask accessToTask, double size) {
        accessToTask.setKey(itemNycbuildingsBean.getKey());
        node = itemNycbuildingsBean.getValue().get_node();
        bounds = new Bounds(node.getCoor().toBBox(size).toRectangle());
        TofixDraw.draw_Node(tofixLayer, node.getCoor());
        Download.Download(downloadOsmTask, bounds, itemNycbuildingsBean.getValue().osm_obj_id(),accessToTask.isShow_pm());
        return accessToTask;
    }

    private AccessToTask work_tigerdelta(ItemTigerdeltaBean itemTigerdeltaBean, AccessToTask accessToTask, double size) {
        accessToTask.setKey(itemTigerdeltaBean.getKey());
        List<List<Node>> list = itemTigerdeltaBean.getValue().get_nodes();
        node = new Node(new LatLon(list.get(0).get(0).getCoor().lat(), list.get(0).get(0).getCoor().lon()));
        bounds = new Bounds(node.getCoor().toBBox(size).toRectangle());
        TofixDraw.draw_line(tofixLayer, node.getCoor(), list);
        
        Download.Download(downloadOsmTask, bounds, 0x0L,accessToTask.isShow_pm());//0x0L = null porque no exixte el id del objeto
        return accessToTask;
    }

    private AccessToTask work_krakatoa(ItemKrakatoaBean itemkrakatoaBean, AccessToTask accessToTask, double size) {
        accessToTask.setKey(itemkrakatoaBean.getKey());
        List<Node> list = itemkrakatoaBean.getValue().get_nodes();
        node = new Node(new LatLon(list.get(0).getCoor().lat(), list.get(0).getCoor().lon()));
        bounds = new Bounds(node.getCoor().toBBox(size).toRectangle());
        TofixDraw.draw_nodes(tofixLayer, node.getCoor(), list);
        Download.Download(downloadOsmTask, bounds, 0x0L,accessToTask.isShow_pm());//0x0L = null porque no exixte el id del objeto
        return accessToTask;
    }

    private AccessToTask work_strava(ItemStrava itemStrava, AccessToTask accessToTask, double size) {
        accessToTask.setKey(itemStrava.getKey());
        node = itemStrava.getValue().get_node();
        bounds = new Bounds(node.getCoor().toBBox(size).toRectangle());
        TofixDraw.draw_Node(tofixLayer, node.getCoor());
        Download.Download(downloadOsmTask, bounds, 0x0L,accessToTask.isShow_pm());
        return accessToTask;
    }

    private AccessToTask work_smallcomponents(ItemSmallcomponents itemSmallcomponents, AccessToTask accessToTask, double size) {
        accessToTask.setKey(itemSmallcomponents.getKey());
         List<List<Node>> list = itemSmallcomponents.getValue().get_nodes();
        node = new Node(new LatLon(list.get(0).get(0).getCoor().lat(), list.get(0).get(0).getCoor().lon()));
        bounds = new Bounds(node.getCoor().toBBox(size).toRectangle());
        TofixDraw.draw_line(tofixLayer, node.getCoor(), list);
        Download.Download(downloadOsmTask, bounds, 0x0L,accessToTask.isShow_pm());//0x0L = null porque no exixte el id del objeto
        return accessToTask;
    }

    public void task_complete(Item item, AccessToTask accessToTask) {
        String message = "Task : " + accessToTask.getTask_name() + " was completed\n"
                + "Total items : " + item.getTaskCompleteBean().getMessage().getValue().getTotal();
        JOptionPane.showMessageDialog(Main.panel, tr(message));
    }

}
