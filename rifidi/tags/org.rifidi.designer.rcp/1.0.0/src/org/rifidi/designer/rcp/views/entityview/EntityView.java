/*
 *  EntityView.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.views.entityview;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.ViewPart;
import org.rifidi.designer.entities.SceneData;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.databinding.ObservableTreeContentProvider;
import org.rifidi.designer.entities.grouping.EntityGroup;
import org.rifidi.designer.services.core.entities.EntitiesService;
import org.rifidi.designer.services.core.entities.FinderService;
import org.rifidi.designer.services.core.entities.SceneDataChangedListener;
import org.rifidi.designer.services.core.entities.SceneDataService;
import org.rifidi.designer.services.core.selection.SelectionService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * View that holds a tree viewer for the entities.
 * 
 * @author Jochen Mader Nov 20, 2007
 * 
 */
public class EntityView extends ViewPart implements ISelectionChangedListener,
		SceneDataChangedListener {
	/**
	 * Eclipse ID
	 */
	public static final String ID = "org.rifidi.designer.rcp.views.entityview.EntityView";
	/**
	 * Logger for this class.
	 */
	private static Log logger = LogFactory.getLog(EntityView.class);
	/**
	 * View container.
	 */
	private Composite container;
	/**
	 * Viewer for the entities.
	 */
	private TreeViewer viewer;
	/**
	 * Reference to the entitiesService.
	 */
	private EntitiesService entitiesService;
	/**
	 * Reference to the selectionService.
	 */
	private SelectionService selectionService;
	/**
	 * Reference to the finderService.
	 */
	private FinderService finderService;
	/**
	 * Reference to the scene data service.
	 */
	private SceneDataService sceneDataService;

	/**
	 * Constructor.
	 */
	public EntityView() {
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout());
		@SuppressWarnings("unused")
		DataBindingContext dbc = new DataBindingContext();
		viewer = new TreeViewer(container, SWT.MULTI);
		viewer.setUseHashlookup(true);
		viewer.setLabelProvider(new WorkbenchLabelProvider());
		viewer.setContentProvider(new ObservableTreeContentProvider());
		createContextMenu();
		int ops = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
		Transfer[] transfers = new Transfer[] { TextTransfer.getInstance() };
		viewer.addDragSupport(ops, transfers, new EntityDragSourceListener(
				viewer));
		viewer.addDropSupport(ops, transfers, new DropTargetListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.dnd.DropTargetListener#dragEnter(org.eclipse.swt.dnd.DropTargetEvent)
			 */
			public void dragEnter(DropTargetEvent event) {
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.dnd.DropTargetListener#dragLeave(org.eclipse.swt.dnd.DropTargetEvent)
			 */
			public void dragLeave(DropTargetEvent event) {
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.dnd.DropTargetListener#dragOperationChanged(org.eclipse.swt.dnd.DropTargetEvent)
			 */
			public void dragOperationChanged(DropTargetEvent event) {
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.dnd.DropTargetListener#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
			 */
			public void dragOver(DropTargetEvent event) {
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.dnd.DropTargetListener#drop(org.eclipse.swt.dnd.DropTargetEvent)
			 */
			public void drop(DropTargetEvent event) {
				if (((TreeItem) event.item).getData() instanceof EntityGroup
						&& !((String) event.data).contains("ID:")) {
					entitiesService.addEntitiesToGroup(
							(EntityGroup) ((TreeItem) event.item).getData(),
							(String) event.data);
				}
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.dnd.DropTargetListener#dropAccept(org.eclipse.swt.dnd.DropTargetEvent)
			 */
			public void dropAccept(DropTargetEvent event) {
				if (!(((TreeItem) event.item).getData() instanceof EntityGroup)) {
					event.detail = DND.ERROR_CANNOT_INIT_DROP;
				}
			}

		});
		getSite().setSelectionProvider(viewer);
		viewer.addSelectionChangedListener(this);
	}

	private void createContextMenu() {
		// Create menu manager.
		MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager mgr) {
				mgr
						.add(new GroupMarker(
								IWorkbenchActionConstants.MB_ADDITIONS));
			}
		});

		// Create menu.
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);

		// Register menu for extension.
		getSite().registerContextMenu(menuMgr, viewer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */

	@Override
	public void setFocus() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		if (!event.getSource().equals(viewer)) {
			viewer.setSelection(event.getSelection());
			return;
		}
		Iterator iter = ((IStructuredSelection) event.getSelection())
				.iterator();
		ArrayList<VisualEntity> hilit = new ArrayList<VisualEntity>();
		while (iter.hasNext()) {
			Object ob = iter.next();
			if (ob instanceof VisualEntity) {
				hilit.add((VisualEntity) ob);
				if(event.getSource().equals(viewer)){
					selectionService.select(hilit, false);
				}
			}
		}
		
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataChangedListener#sceneDataChanged(org.rifidi.designer.entities.SceneData)
	 */
	@Override
	public void sceneDataChanged(SceneData sceneData) {
		viewer.setInput(sceneData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataChangedListener#destroySceneData(org.rifidi.designer.entities.SceneData)
	 */
	@Override
	public void destroySceneData(SceneData sceneData) {
		viewer.setInput(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		viewer.removeSelectionChangedListener(this);
		selectionService.removeSelectionChangedListener(this);
		sceneDataService.removeSceneDataChangedListener(this);
	}

	/**
	 * @param entitiesService
	 *            the entitiesService to set
	 */
	@Inject
	public void setEntitiesService(EntitiesService entitiesService) {
		this.entitiesService = entitiesService;
	}

	/**
	 * @param selectionService
	 *            the selectionService to set
	 */
	@Inject
	public void setSelectionService(SelectionService selectionService) {
		this.selectionService = selectionService;
		selectionService.addSelectionChangedListener(this);
	}

	/**
	 * @param finderService
	 *            the finderService to set
	 */
	@Inject
	public void setFinderService(FinderService finderService) {
		this.finderService = finderService;
	}

	/**
	 * @param sceneDataService
	 *            the sceneDataService to set
	 */
	@Inject
	public void setSceneDataService(SceneDataService sceneDataService) {
		this.sceneDataService = sceneDataService;
		sceneDataService.addSceneDataChangedListener(this);
		if (sceneDataService.getCurrentSceneData() != null) {
			sceneDataChanged(sceneDataService.getCurrentSceneData());
		}
	}

}
