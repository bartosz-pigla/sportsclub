// export const publicPath = 'public';
// export const customerPath = 'customer';
// export const adminPath = 'admin';
//
// //PUBLIC PATHS
// export const signInRelativePath = 'sign-in';
// export const signInAbsolutePath = getAbsolutePath(publicPath, signInRelativePath);
//
// export const unauthorizedRelativePath = 'unauthorized';
// export const unauthorizedAbsolutePath = getAbsolutePath(publicPath, unauthorizedRelativePath);
//
// export const notFoundRelativePath = 'not-found';
// export const notFoundAbsolutePath = getAbsolutePath(publicPath, notFoundRelativePath);
//
// //CUSTOMER PATHS
// export const homeRelativePath = 'home-page';
// export const homeAbsolutePath = getAbsolutePath(customerPath, homeRelativePath);
//
// function getAbsolutePath(parentPath, childPath) {
//   return parentPath + '/' + childPath;
// }

export const publicPath = 'public';
export const customerPath = 'customer';
export const adminPath = 'admin';

//PUBLIC PATHS
export const signInRelativePath = 'sign-in';
export const signInAbsolutePath = getAbsolutePath(publicPath, signInRelativePath);

export const unauthorizedRelativePath = 'unauthorized';
export const unauthorizedAbsolutePath = getAbsolutePath(publicPath, unauthorizedRelativePath);

export const notFoundRelativePath = 'not-found';
export const notFoundAbsolutePath = getAbsolutePath(publicPath, notFoundRelativePath);

//CUSTOMER PATHS
export const homeRelativePath = 'home-page';
export const homeAbsolutePath = getAbsolutePath(customerPath, homeRelativePath);

function getAbsolutePath(parentPath, childPath) {
  return parentPath + '/' + childPath;
}
