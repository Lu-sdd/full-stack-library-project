export const Pagination: React.FC<{
    currentPage: number, 
    totalPages: number, 
    paginate: any // Callback function to handle page changes
}> = (props) => {
    // Array to store the page numbers to be displayed
    const pageNumbers = [];

    // If the current page is 1
    if (props.currentPage === 1) {
        // Add the current page to the pageNumbers array
        pageNumbers.push(props.currentPage);

        // If there is at least one more page after the current page, add the next page to the array
        if (props.totalPages >= props.currentPage + 1) {
            pageNumbers.push(props.currentPage + 1);
        }

        // If there are at least two more pages after the current page, add the second next page to the array
        if (props.totalPages >= props.currentPage + 2) {
            pageNumbers.push(props.currentPage + 2);
        }
    }

    // If the current page is greater than 1
    else if (props.currentPage > 1) {
        // If the current page is greater than or equal to 3, add the two previous pages to the array
        if (props.currentPage >= 3) {
            pageNumbers.push(props.currentPage - 2);
            pageNumbers.push(props.currentPage - 1);
        } else {
            // If the current page is less than 3, add the previous page to the array
            pageNumbers.push(props.currentPage - 1);
        }

        // Add the current page to the array
        pageNumbers.push(props.currentPage);

        // If there is at least one more page after the current page, add the next page to the array
        if (props.totalPages >= props.currentPage + 1) {
            pageNumbers.push(props.currentPage + 1);
        }

        // If there are at least two more pages after the current page, add the second next page to the array
        if (props.totalPages >= props.currentPage + 2) {
            pageNumbers.push(props.currentPage + 2);
        }
    }

    // Return the Pagination component
    return (
        <nav aria-label="...">
            <ul className="pagination">
                {/* First Page button */}
                <li className="page-item" onClick={() => props.paginate(1)}>
                    <button className="page-link">
                        First Page
                    </button>
                </li>
                {/* Map through pageNumbers array and display page number buttons */}
                {pageNumbers.map(number => (
                    <li key={number} onClick={() => props.paginate(number)}
                        className={'page-item ' + (props.currentPage === number ? 'active' : '')}>
                        <button className="page-link">
                            {number}
                        </button>
                    </li>
                ))}
                {/* Last Page button */}
                <li className="page-item" onClick={() => props.paginate(props.totalPages)}>
                    <button className="page-link">
                        Last Page
                    </button>
                </li>
            </ul>
        </nav>
    );
};
