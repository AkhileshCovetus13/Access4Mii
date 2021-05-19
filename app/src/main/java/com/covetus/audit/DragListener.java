package com.covetus.audit;

import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.View;

import java.util.List;

import static com.covetus.audit.SelectMainLocationActivity.dragNew;
import static com.covetus.audit.SelectMainLocationActivity.mTxtLocationDesc;
import static com.covetus.audit.SelectMainLocationActivity.meMapDesc;

public class DragListener implements View.OnDragListener {

    private boolean isDropped = false;
    private Listener listener;
    DragListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DROP:
                isDropped = true;
                int positionTarget = -1;

                View viewSource = (View) event.getLocalState();
                int viewId = v.getId();
                final int flItem = R.id.frame_layout_item;
                final int tvEmptyListTop = R.id.tvEmptyListTop;
                final int tvEmptyListBottom = R.id.tvEmptyListBottom;
                final int rvTop = R.id.rvTop;
                final int rvBottom = R.id.rvBottom;

                switch (viewId) {
                    case flItem:
                    case tvEmptyListTop:
                    case tvEmptyListBottom:
                    case rvTop:
                    case rvBottom:

                        RecyclerView target;
                        switch (viewId) {
                            case tvEmptyListTop:
                            case rvTop:
                                target = v.getRootView().findViewById(rvTop);
                                break;
                            case tvEmptyListBottom:
                            case rvBottom:
                                target = v.getRootView().findViewById(rvBottom);
                                break;
                            default:
                                target = (RecyclerView) v.getParent();
                                positionTarget = (int) v.getTag();
                        }

                        if (viewSource != null) {
                            RecyclerView source = (RecyclerView) viewSource.getParent();
                            DragListAdapter adapterSource = (DragListAdapter) source.getAdapter();
                            int positionSource = (int) viewSource.getTag();
                            int sourceId = source.getId();

                            String list = adapterSource.getList().get(positionSource);
                            List<String> listSource = adapterSource.getList();
                            listSource.remove(positionSource);
                            adapterSource.updateList(listSource);
                            adapterSource.notifyDataSetChanged();
                            DragListAdapter adapterTarget = (DragListAdapter) target.getAdapter();
                            List<String> customListTarget = adapterTarget.getList();
                            if (positionTarget >= 0) {
                                customListTarget.add(positionTarget, list);
//                                SelectMainLocationActivity.meMap.put(list,"");
                                System.out.println("<><><>call1");
                            } else {
                                customListTarget.add(list);
                                SelectMainLocationActivity.meMap.put(list,"1");
                                System.out.println("<><><>call2");
                                dragNew(list);
                            }
                            mTxtLocationDesc.setText(meMapDesc.get(list));
                            SelectMainLocationActivity.mInfoText.setText(list+" "+SelectMainLocationActivity.activity.getString(R.string.mText_desc));
                            System.out.println("<><><desc"+meMapDesc.get(list));
                            adapterTarget.updateList(customListTarget);
                            adapterTarget.notifyDataSetChanged();

                          /*  if (sourceId == rvBottom && adapterSource.getItemCount() < 1) {
                                listener.setEmptyListBottom(true);
                            }
                            if (viewId == tvEmptyListBottom) {
                                listener.setEmptyListBottom(false);
                            }
                            if (sourceId == rvTop && adapterSource.getItemCount() < 1) {
                                listener.setEmptyListTop(true);
                            }
                            if (viewId == tvEmptyListTop) {
                                listener.setEmptyListTop(false);
                            }*/
                        }
                        break;
                }
                break;
        }

        if (!isDropped && event.getLocalState() != null) {
            ((View) event.getLocalState()).setVisibility(View.VISIBLE);
        }
        return true;
    }
}