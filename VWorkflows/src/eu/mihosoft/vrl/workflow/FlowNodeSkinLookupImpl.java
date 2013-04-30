/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.vrl.workflow;

import java.util.List;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
class FlowNodeSkinLookupImpl implements FlowNodeSkinLookup {

    private VFlow root;

    public FlowNodeSkinLookupImpl(VFlow root) {
        this.root = root;
    }

    @Override
    public List<VNodeSkin> getById(String globalId) {

        List<VNodeSkin> result = getNodeByGlobalId(root, globalId);

        return result;
    }

    private List<VNodeSkin> getNodeByGlobalId(VFlow parent, String id) {

        List<VNodeSkin> s = parent.getNodeSkinsById(id);

        if (s != null) {
            return s;
        }

        for (VFlow c : parent.getSubControllers()) {
            s = getNodeByGlobalId(c, id);

            if (s != null) {
                return s;
            }
        }

        return null;
    }

    private VNodeSkin getNodeByGlobalId(SkinFactory skinFactory, VFlow parent, String id) {

        System.out.println(">> searching: " + id);

        List<VNodeSkin> s = parent.getNodeSkinsById(id);

        VNodeSkin result = getBySkinFactory(skinFactory, s);

        System.out.println(" --> get-skin: " + result);

        if (result != null) {
            return result;
        }

        for (VFlow c : parent.getSubControllers()) {
            for (SkinFactory sF : c.getSkinFactories()) {
                
                System.out.println(" --> parent = " + sF.getParent() + ", skinFactory = " + skinFactory);

                if (sF == skinFactory) {
                    System.out.println(" --> searching in subfactory");
                    result = getNodeByGlobalId(sF, c, id);
                }

                if (result != null) {
                    return result;
                }
            }
        }
        
        System.out.println(" --> nothing found :(");

        return null;
    }

    private VNodeSkin getBySkinFactory(SkinFactory skinFactory, List<VNodeSkin> candidates) {

        for (VNodeSkin vNodeSkin : candidates) {

            if (vNodeSkin.getSkinFactory() == skinFactory) {
                return vNodeSkin;
            }
        }

        return null;
    }

    @Override
    public VNodeSkin getById(SkinFactory skinFactory, String globalId) {
        VNodeSkin result = getNodeByGlobalId(skinFactory, root, globalId);

        if (result == null) {
            System.out.println("getById(): " + result);
        } else {
            System.out.println("NOT FOUND: getById(): " + null);
        }

        return result;
    }
}
