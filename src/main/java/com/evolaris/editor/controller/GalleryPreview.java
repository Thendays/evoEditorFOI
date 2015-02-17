package com.evolaris.editor.controller;

import com.evolaris.editor.model.VideoPageResource;
import com.evolaris.editor.model.interfaces.IGallery;
import com.evolaris.editor.model.interfaces.IPage;
import com.evolaris.editor.model.interfaces.IPageResource;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class ImagePanel extends JPanel {
	Image img = null;
	
	ImagePanel(String image) {
		MediaTracker mt = new MediaTracker(this);
		img = Toolkit.getDefaultToolkit().getImage(image);
		mt.addImage(img, 0);
		
		try{
			mt.waitForAll();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int imgWidth = img.getWidth(null);
		int imgHeight = img.getHeight(null);
		g.drawImage(img, 1, 1, null);
	}
}

public class GalleryPreview {
	
	private static final int DEFAULT_WIDTH = 613;
	private static final int DEFAULT_HEIGHT = 143;
	private static final int JFRAME_HEIGHT = 300;
	
	private JFrame previewFrame;
	private JPanel parentPanel;
	
	private IGallery gallery;
	private IPage currentPage;
	
    public GalleryPreview() {
    	previewFrame = new JFrame();
    	parentPanel = new JPanel();
    	
    	
//    	ImagePanel img = new ImagePanel("AVL483_Hinweis_Reinigung_1_split1.jpg");
//        img.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
//        parentPanel.add(img);
    	
        
    	parentPanel.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
    	previewFrame.setSize(DEFAULT_WIDTH, JFRAME_HEIGHT);
    	previewFrame.add(parentPanel);
    	previewFrame.setVisible(true);
    }

    public void showPageThumbnail(IPage page) {
    	parentPanel.removeAll();
    	
        ArrayList<IPageResource> resources = page.getPageResources();

        for (IPageResource resource : resources) {
            if (resource.getName().equals("image")) {
                if (!resource.getAttributeValue("path").equals("")) {
                    ImagePanel img = new ImagePanel(resource.getAttributeValue("path"));
                    img.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
                    parentPanel.add(img);
                }
            } else if (resource instanceof VideoPageResource) {
                if (!resource.getAttributeValue("path").equals("")) {
                    // prikaži video
                }
            }
        }

        for (IPageResource resource : resources) {
            if (resource.getName().equals("text")) {
                if (!resource.getContent().equals("")) {
                    JLabel text = new JLabel(resource.getContent());
                    text.setFont(new Font("Verdana",1,20));
                    
                    parentPanel.setLayout(new BoxLayout(parentPanel, BoxLayout.Y_AXIS));
                    parentPanel.add(text);
                }
            }
        }
        
        parentPanel.revalidate();
        parentPanel.repaint();
    }

    public void previewGallery(IGallery gallery) {
    	this.gallery = gallery;
    	parentPanel.removeAll();
    	
    	previewFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    	
    	ArrayList<IPage> pages = this.gallery.getChildPageList(this.gallery.getID());
    	ArrayList<IPageResource> resources = pages.get(0).getPageResources();
    	currentPage = pages.get(0);
    	
    	for (IPageResource resource : resources) {
            if (resource.getName().equals("image")) {
                if (!resource.getAttributeValue("path").equals("")) {
                    ImagePanel img = new ImagePanel(resource.getAttributeValue("path"));
                    img.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
                    parentPanel.add(img);
                }
            } else if (resource instanceof VideoPageResource) {
                if (!resource.getAttributeValue("path").equals("")) {
                    // prikaži video
                }
            }
        }

        for (IPageResource resource : resources) {
            if (resource.getName().equals("text")) {
                if (!resource.getContent().equals("")) {
                    JLabel text = new JLabel(resource.getContent());
                    text.setFont(new Font("Verdana",1,20));
                    
                    parentPanel.setLayout(new BoxLayout(parentPanel, BoxLayout.Y_AXIS));
                    parentPanel.add(text);
                }
            }
        }
        
        parentPanel.revalidate();
        parentPanel.repaint();
    }
    
    public void nextPage() {
    	for (int i = 0; i < gallery.getChildPageList(currentPage.getParentID()).size(); i++) {
    		if (gallery.getChildPageList(currentPage.getParentID()).get(i).getId() == currentPage.getId() && 
    				i != (gallery.getChildPageList(currentPage.getParentID()).size() - 1)) {
    			currentPage = gallery.getChildPageList(currentPage.getParentID()).get(i + 1);
    			break;
    		}
    	}
    	parentPanel.removeAll();
    	ArrayList<IPageResource> resources = currentPage.getPageResources();
    	
    	for (IPageResource resource : resources) {
            if (resource.getName().equals("image")) {
                if (!resource.getAttributeValue("path").equals("")) {
                    ImagePanel img = new ImagePanel(resource.getAttributeValue("path"));
                    img.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
                    parentPanel.add(img);
                }
            } else if (resource instanceof VideoPageResource) {
                if (!resource.getAttributeValue("path").equals("")) {
                    // prikaži video
                }
            }
        }

        for (IPageResource resource : resources) {
            if (resource.getName().equals("text")) {
                if (!resource.getContent().equals("")) {
                    JLabel text = new JLabel(resource.getContent());
                    text.setFont(new Font("Verdana",1,20));
                    
                    parentPanel.setLayout(new BoxLayout(parentPanel, BoxLayout.Y_AXIS));
                    parentPanel.add(text);
                }
            }
        }
        
        parentPanel.revalidate();
        parentPanel.repaint();
    }
    
    public void prevPage() {
    	for (int i = (gallery.getChildPageList(currentPage.getParentID()).size() - 1); i >= 0; i--) {
    		if (gallery.getChildPageList(currentPage.getParentID()).get(i).getId() == currentPage.getId() && 
    				i != 0) {
    			currentPage = gallery.getChildPageList(currentPage.getParentID()).get(i - 1);
    			break;
    		}
    	}
    	parentPanel.removeAll();
    	ArrayList<IPageResource> resources = currentPage.getPageResources();
    	
    	for (IPageResource resource : resources) {
            if (resource.getName().equals("image")) {
                if (!resource.getAttributeValue("path").equals("")) {
                    ImagePanel img = new ImagePanel(resource.getAttributeValue("path"));
                    img.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
                    parentPanel.add(img);
                }
            } else if (resource instanceof VideoPageResource) {
                if (!resource.getAttributeValue("path").equals("")) {
                    // prikaži video
                }
            }
        }

        for (IPageResource resource : resources) {
            if (resource.getName().equals("text")) {
                if (!resource.getContent().equals("")) {
                    JLabel text = new JLabel(resource.getContent());
                    text.setFont(new Font("Verdana",1,20));
                    
                    parentPanel.setLayout(new BoxLayout(parentPanel, BoxLayout.Y_AXIS));
                    parentPanel.add(text);
                }
            }
        }
        
        parentPanel.revalidate();
        parentPanel.repaint();
    }
    
    public void enterSubPage() {
    	if (gallery.getChildPageList(currentPage.getId()).size() != 0) {
    		currentPage = gallery.getChildPageList(currentPage.getId()).get(0);
    		
    		parentPanel.removeAll();
        	ArrayList<IPageResource> resources = currentPage.getPageResources();
        	
        	for (IPageResource resource : resources) {
                if (resource.getName().equals("image")) {
                    if (!resource.getAttributeValue("path").equals("")) {
                        ImagePanel img = new ImagePanel(resource.getAttributeValue("path"));
                        img.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
                        parentPanel.add(img);
                    }
                } else if (resource instanceof VideoPageResource) {
                    if (!resource.getAttributeValue("path").equals("")) {
                        // prikaži video
                    }
                }
            }

            for (IPageResource resource : resources) {
                if (resource.getName().equals("text")) {
                    if (!resource.getContent().equals("")) {
                        JLabel text = new JLabel(resource.getContent());
                        text.setFont(new Font("Verdana",1,20));
                        
                        parentPanel.setLayout(new BoxLayout(parentPanel, BoxLayout.Y_AXIS));
                        parentPanel.add(text);
                    }
                }
            }
            
            parentPanel.revalidate();
            parentPanel.repaint();
    	}
    }
}

class ImageComponent extends JComponent {
    private Image image;

    public ImageComponent(String img) {
        File imag = new File(img);
        try {
            image = ImageIO.read(imag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paintComponent (Graphics g) {
        if (image == null) return;
        int imageWidth = image.getWidth(this);
        int imageHeight = image.getHeight(this);

        g.drawImage(image, 50, 50, this);
    }
}

class ImageFrame extends JFrame {
    public ImageFrame() {
        ImageComponent component = new ImageComponent("/home/tomi/EvoEditor/AVL483_Warnung_Laserstrahl_split4_confirm.jpg");
        add(component);
    }
}
