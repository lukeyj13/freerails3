/*
 * Created on 26-Jul-2005
 *
 */
package jfreerails.util;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

public class List3DDiff<T> extends ListXDDiffs<T> implements List3D<T> {

    private static final long serialVersionUID = 14976913635251766L;

    private final List3D<T> underlyingList;

    public List3DDiff(SortedMap<ListKey, Object> diffs, List3D<T> list,
            Enum listID) {
        super(diffs, listID);
        this.underlyingList = list;
    }

    public int addD1() {
        return super.addDimension();
    }

    public int addD2(int d1) {
        return super.addDimension(d1);
    }

    public int addD3(int d1, int d2, T element) {
        return super.addElement(element, d1, d2);
    }

    public T get(int d1, int d2, int d3) {
        return super.get(d1, d2, d3);
    }

    @Override
    Object getUnderlyingList() {
        return underlyingList;
    }

    @Override
    int getUnderlyingSize(int... dim) {
        if (dim.length == 0)
            return underlyingList.sizeD1();
        if (dim.length == 1) {
            if (underlyingList.sizeD1() <= dim[0])
                return -1;

            return underlyingList.sizeD2(dim[0]);
        }
        if (dim.length == 2) {
            if (underlyingList.sizeD1() <= dim[0])
                return -1;
            if (underlyingList.sizeD2(dim[0]) <= dim[1])
                return -1;
            return underlyingList.sizeD3(dim[0], dim[1]);

        }
        throw new IllegalArgumentException(String.valueOf(dim.length));
    }

    public void removeLastD1() {
        super.removeLastList();

    }

    public void removeLastD2(int d1) {
        super.removeLastList(d1);
    }

    public T removeLastD3(int d1, int d2) {
        return super.removeLast(d1, d2);
    }

    public void set(int d1, int d2, int d3, T element) {
        super.set(element, d1, d2, d3);

    }

    public int sizeD1() {
        return super.size();
    }

    public int sizeD2(int d1) {
        return super.size(d1);
    }

    public int sizeD3(int d1, int d2) {
        return super.size(d1, d2);
    }

    @Override
    T uGet(int... i) {
        if (i.length != 3)
            throw new IllegalArgumentException();
        return underlyingList.get(i[0], i[1], i[2]);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof List3D))
            return false;
        return Lists.equals(this, (List3D) obj);
    }

    @Override
    public int hashCode() {
        return sizeD1();
    }

    public List<T> get(int d1, int d2) {
        List<T> list = new ArrayList<T>();
        for(int d3 = 0; d3 < sizeD3(d1, d2); d3++) {
            list.add(get(d1, d2, d3));
        }
        return list;
    }

}
