package io.schinzel.web_blocks.web.routes_overview

/**
 * Generates HTML template structure and CSS styles for the routes overview page.
 * Handles the static HTML header, footer, and styling.
 *
 * Written by Claude Sonnet 4
 */
class HtmlTemplateGenerator {
    /**
     * Generates HTML header with title and styles
     */
    fun generateHeader(): String =
        """
        <!DOCTYPE html>
        <html>
        <head>
            <title>WebBlocks Routes Overview</title>
            <style>
                body { 
                    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
                    background-color: #f5f7fa;
                    margin: 0;
                    padding: 30px;
                    line-height: 1.6;
                    color: #2d3748;
                }
                
                h1 { 
                    color: #2d3748; 
                    font-size: 2.5rem;
                    font-weight: 600;
                    margin-bottom: 10px;
                }
                
                h2 { 
                    color: #4a5568; 
                    font-size: 1.5rem;
                    font-weight: 600;
                    margin: 40px 0 20px 0;
                    border-bottom: 2px solid #e2e8f0;
                    padding-bottom: 10px;
                }
                
                .route-container, .block-container, .api-container { 
                    background: white;
                    border-radius: 8px;
                    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                    margin-bottom: 16px;
                    border: 1px solid #e2e8f0;
                    overflow: hidden;
                }
                
                .route-header, .block-header, .api-header { 
                    padding: 12px 16px; 
                    cursor: pointer;
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    transition: background-color 0.2s ease;
                    border-bottom: 1px solid #e2e8f0;
                }
                
                .route-header { 
                    background: #f8fafc;
                    border-left: 4px solid #3182ce;
                }
                .route-header:hover { background: #edf2f7; }
                
                .block-header { 
                    background: #fffbf0;
                    border-left: 4px solid #ed8936;
                }
                .block-header:hover { background: #fef5e7; }
                
                .api-header { 
                    background: #f0fff4;
                    border-left: 4px solid #38a169;
                }
                .api-header:hover { background: #e6fffa; }
                
                .route-path { 
                    font-family: "SF Mono", Monaco, "Cascadia Code", "Roboto Mono", Consolas, "Courier New", monospace;
                    font-weight: 600;
                    font-size: 0.95rem;
                    color: #2d3748;
                }
                
                .expand-icon { 
                    font-size: 1.2rem;
                    transition: transform 0.2s ease;
                    color: #718096;
                }
                .expand-icon.expanded { transform: rotate(90deg); }
                
                .route-details { 
                    padding: 0;
                    background: #ffffff;
                    display: none;
                }
                .route-details.expanded { display: block; }
                
                .details-content {
                    padding: 12px 16px;
                }
                
                .info-table {
                    width: 100%;
                    border-collapse: collapse;
                    margin: 16px 0;
                    background: white;
                    border-radius: 6px;
                    overflow: hidden;
                    box-shadow: 0 1px 3px rgba(0,0,0,0.1);
                }
                
                .info-table th {
                    background: #f7fafc;
                    padding: 8px 12px;
                    text-align: left;
                    font-weight: 600;
                    color: #4a5568;
                    border-bottom: 1px solid #e2e8f0;
                    font-size: 0.875rem;
                }
                
                .info-table td {
                    padding: 8px 12px;
                    border-bottom: 1px solid #f1f5f9;
                    font-family: "SF Mono", Monaco, "Cascadia Code", "Roboto Mono", Consolas, "Courier New", monospace;
                    font-size: 0.875rem;
                    color: #2d3748;
                }
                
                .params-table {
                    width: 100%;
                    border-collapse: collapse;
                    margin: 16px 0;
                    background: white;
                    border-radius: 6px;
                    overflow: hidden;
                    box-shadow: 0 1px 3px rgba(0,0,0,0.1);
                }
                
                .params-table th {
                    background: #f7fafc;
                    padding: 8px 12px;
                    text-align: left;
                    font-weight: 600;
                    color: #4a5568;
                    border-bottom: 1px solid #e2e8f0;
                    font-size: 0.875rem;
                }
                
                .params-table td {
                    padding: 8px 12px;
                    border-bottom: 1px solid #f1f5f9;
                    font-size: 0.875rem;
                }
                
                .param-name {
                    font-family: "SF Mono", Monaco, "Cascadia Code", "Roboto Mono", Consolas, "Courier New", monospace;
                    font-weight: 600;
                    color: #2d3748;
                }
                
                .param-type {
                    font-family: "SF Mono", Monaco, "Cascadia Code", "Roboto Mono", Consolas, "Courier New", monospace;
                    color: #805ad5;
                    background: #faf5ff;
                    padding: 2px 6px;
                    border-radius: 4px;
                    font-size: 0.75rem;
                }
                
                .blocks-label, .apis-label { 
                    margin: 16px 0 8px 0;
                    font-weight: 600; 
                    color: #4a5568;
                    font-size: 1rem;
                }
                
                .nested-blocks, .nested-apis {
                    margin-left: 0;
                }
                
                .page-bottom {
                    margin-bottom: 40px;
                }
            </style>
            <script>
                function toggleExpand(element) {
                    const details = element.nextElementSibling;
                    const icon = element.querySelector('.expand-icon');
                    
                    if (details.classList.contains('expanded')) {
                        details.classList.remove('expanded');
                        icon.classList.remove('expanded');
                    } else {
                        details.classList.add('expanded');
                        icon.classList.add('expanded');
                    }
                }
            </script>
        </head>
        <body>
            <h1>WebBlocks Routes Overview</h1>
        """.trimIndent()

    /**
     * Generates HTML footer
     */
    fun generateFooter(): String =
        """
        <div class="page-bottom"></div>
        </body>
        </html>
        """.trimIndent()
}
